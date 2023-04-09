package otus.homework.coroutines

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
): ViewModel() {

    private val _viewState = MutableLiveData<Result>()
    val viewState: LiveData<Result> = _viewState

    private lateinit var job: Job

    fun getFactAndImage() {
        if (::job.isInitialized) {
            job.cancel()
        }
        job = viewModelScope.launch(handler) {
            ensureActive()
            val factAsync = async { catsService.getCatFact() }
            val imageAsync = async { imageService.getImage() }
            _viewState.value = Success(
                CatData(
                    imageAsync.await().url,
                    factAsync.await().fact
                )
            )
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
        _viewState.value = when (exception) {
            is SocketTimeoutException -> SocketExceptionError
            else -> Error(exception.message.toString())
        }
    }

    class Factory(
        private val catsService: CatsService,
        private val imageService: ImageService
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CatsViewModel(
                catsService,
                imageService
            ) as T
        }
    }

}