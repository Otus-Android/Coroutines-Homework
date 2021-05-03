package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsServiceFact: CatsService,
    private val catsServiceImage: CatsService
): ViewModel() {
    private var _catsResponse = MutableLiveData<Result<FactImage>>()
    val catsResponse: LiveData<Result<FactImage>>
        get() = _catsResponse

    fun getCatFactImage(){
        viewModelScope.launch(SupervisorJob() + CoroutineExceptionHandler{
            coroutineContext, throwable -> CrashMonitor.trackWarning()
        }) {
            try {
                val factResponse = getCatFactResponse()
                val imageResponse = getCatImageResponse()
                if (factResponse != null && imageResponse != null) {
                    val factImage = FactImage(factResponse, imageResponse)
                    _catsResponse.value = Result.Success(factImage)
                } else{
                    _catsResponse.value = Result.Error("Что то пошло не так", null)
                }

            } catch (e: java.net.SocketTimeoutException) {
                _catsResponse.value = Result.Error("Не удалось получить ответ от сервером", e)
            } catch (e: Exception) {
                _catsResponse.value = Result.Error(e.message.toString(), e)
                e.printStackTrace()
            }
        }
    }

    private suspend fun getCatFactResponse(): Fact {
        return catsServiceFact.getCatFact()
    }


    private suspend fun getCatImageResponse(): Image {
        return catsServiceImage.getCatImage()
    }
}

sealed class Result<out T>{
    data class Success<out R>(val value: R): Result<R>()
    data class Error(
        val message: String,
        val throwable: Throwable?
    ): Result<Nothing>()
}