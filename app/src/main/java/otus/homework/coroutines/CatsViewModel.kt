package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel: ViewModel() {

    private val _catsFactData = MutableLiveData<Result<CatResultData>>()
    val catsFactData: LiveData<Result<CatResultData>> = _catsFactData

    fun onInitComplete(catsService: CatsService, awsService: CatsService) {

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning()
            _catsFactData.postValue(Result.Error(null))
        }

        val fact = viewModelScope.async(Dispatchers.IO) {
            catsService.getCatFact()
        }

        val picture = viewModelScope.async(Dispatchers.IO) {
            awsService.getCatPicture()
        }

        viewModelScope.launch(exceptionHandler) {
            try {
                _catsFactData.value = Result.Success(CatResultData(picture.await().fileUrl, fact.await().text))
            } catch (e: SocketTimeoutException) {
                _catsFactData.value = Result.Error(null, "Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                e.message?.let {
                    Result.Error(null, it)
                }
            }
        }
    }
}