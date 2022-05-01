package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsTextService: CatsService,
    private val catsImageService: CatsImageService
): ViewModel() {
    private val _catsData = MutableLiveData<Result<CatsData>>()
    val catsData: LiveData<Result<CatsData>>
        get() = _catsData

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning()
        }) {
            coroutineScope {
                try {
                    val catsText = async (Dispatchers.IO) { catsTextService.getCatFact() }
                    val catsImage = async (Dispatchers.IO) { catsImageService.getCatImage() }
                    _catsData.postValue(Result.Success(CatsData.create(catsText.await(), catsImage.await())))
                } catch (e: SocketTimeoutException) {
                    _catsData.postValue(Result.Failure("Не удалось получить ответ от сервера", e))
                } catch (e: Exception) {
                    CrashMonitor.trackWarning()
                    _catsData.postValue(Result.Failure("Произошла ошибка", e))
                }
            }

        }
    }

    class Factory(
        private val catsTextService: CatsService,
        private val catsImageService: CatsImageService
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsTextService, catsImageService) as T
        }
    }
}


