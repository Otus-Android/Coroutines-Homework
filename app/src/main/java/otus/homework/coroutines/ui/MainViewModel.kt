package otus.homework.coroutines.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatInfo
import otus.homework.coroutines.repository.IFactRepository
import otus.homework.coroutines.repository.IPODRepository
import java.net.SocketTimeoutException

class MainViewModel(
    private val factRepository: IFactRepository,
    private val podRepository: IPODRepository
) : ViewModel() {

    private companion object {
        const val API_KEY = "2zmSP2v2vx8eYc1Y3mKL7UhSag0kIj7pdkYf0v1p"
    }

    private val liveDataForViewToObserve = MutableLiveData<CatInfoState>()
    val liveData: LiveData<CatInfoState> = liveDataForViewToObserve

    private fun handleError(throwable: Throwable) {
        liveDataForViewToObserve.value = CatInfoState.Error(throwable)
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        handleError(throwable)
    }

    fun getCatInfo() {
        viewModelScope.launch(exceptionHandler) {
            supervisorScope {
                try {
                    val fact = async { factRepository.getFact() }
                    val picture = async { podRepository.getPicture(apiKey = API_KEY) }
                    val result = CatInfo(fact = fact.await().fact, pictureUrl = picture.await().url ?: "")
                    liveDataForViewToObserve.value = CatInfoState.Success(result)
                } catch (e: SocketTimeoutException) {
                    handleError(e)
                }
            }
        }
    }

    class Factory(
        private val factRepository: IFactRepository,
        private val podRepository: IPODRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(factRepository, podRepository) as T
        }

    }
}
