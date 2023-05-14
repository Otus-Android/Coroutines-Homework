package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _viewState = MutableLiveData<Result>()
    val viewState: LiveData<Result> = _viewState

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler{ _,error ->
            CrashMonitor.trackWarning()
            _viewState.value =
                Error(error.message.toString())
        }) {
            supervisorScope {
                val fact = async { catsService.getCatFact() }
                val img = async { imageService.getCatImg() }
                try {
                    _viewState.value =
                        Success<Data>(Data(fact.await().text, img.await()[0].url))
                } catch (error: SocketTimeoutException) {
                    CrashMonitor.trackWarning()
                    _viewState.value =
                        Error(error.message.toString())
                }
            }
        }
    }


}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, imageService) as T
    }

}