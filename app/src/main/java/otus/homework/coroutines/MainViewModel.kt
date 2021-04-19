package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    val resultLiveData = MutableLiveData<Result>()
    private var factJob: Job? = null
    private var refreshJob: Job? = null
    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            factJob?.cancelAndJoin()
            factJob = launch {
                try {
                    val result = withContext(Dispatchers.IO) {
                        catsService.getCatFact()
                    }
                    resultLiveData.postValue(Result.Success(result))
                } catch (ex: SocketTimeoutException) {
                    resultLiveData.postValue(Result.Error(ex.localizedMessage ?: "error"))
                }
            }
        }
    }

    fun loadFactAndImage() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            refreshJob?.cancelAndJoin()
            refreshJob = launch {
                val factResult = async { catsService.getCatFact() }
                val imageResult = async { imageService.getCatImage() }
                try {
                    val factWithImage = factResult.await().copy(image = imageResult.await().fileName)
                    resultLiveData.postValue(Result.Success(factWithImage))
                } catch (ex: SocketTimeoutException) {
                    resultLiveData.postValue(Result.Error(ex.localizedMessage ?: "error"))
                }
            }
        }
    }

    sealed class Result {
        data class Success(val data: Fact) : Result()
        data class Error(val message: String) : Result()
    }

}