package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<Result<CatInfo>>()
    val catsLiveData: LiveData<Result<CatInfo>> get() = _catsLiveData

    fun onInitComplete() {
        val handler = CoroutineExceptionHandler { _, exception ->
            exception.message?.let { CrashMonitor.trackWarning("Handler catch: $it") }
        }
        viewModelScope.launch(handler) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsImageService.getCatImage() }
                val catInfo = CatInfo(fact.await().fact, image.await().catImageUri)
                _catsLiveData.value = Result.Success(catInfo)
            } catch (e: SocketTimeoutException) {
                _catsLiveData.value = Result.Error("Не удалось получить ответ от сервера")
            } catch (e: CancellationException) {
                CrashMonitor.trackWarning("Coroutine was cancelled")
            }

        }
    }
}

class ViewModelFactory(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, catsImageService) as T
    }

}