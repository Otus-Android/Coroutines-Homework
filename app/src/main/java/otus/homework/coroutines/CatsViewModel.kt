/**
 * Created by Ilia Shelkovenko on 23.01.2022.
 */

package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {
    private lateinit var getFactJob: Job
    private val _catsPresentation = MutableLiveData<Result<CatsPresentation>>()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning(throwable)
    }
    val catsPresentation: LiveData<Result<CatsPresentation>> = _catsPresentation

    fun onInitComplete() {
        getFactJob = viewModelScope.launch(exceptionHandler) {
            val fact = async(Dispatchers.IO) { catsService.getCatFact() }
            val imageUrl = async(Dispatchers.IO) { imageService.getCatImage() }
            try {
                _catsPresentation.value =
                    Result.Success(CatsPresentation(fact.await(), imageUrl.await().file))
            } catch (ex: java.net.SocketTimeoutException) {
                _catsPresentation.value = Result.Error("Не удалось получить ответ от сервера")
            }
        }
    }

    fun cancelJob() {
        getFactJob.cancel()
    }

}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, imageService) as T
    }
}
