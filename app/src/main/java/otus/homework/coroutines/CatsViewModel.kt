package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException

sealed class Result

class Success<T>(val data: T) : Result()
class Error(val errorMsg: String) : Result()

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val catInfo: MutableLiveData<Result> = MutableLiveData<Result>()
    private val exceptionHandler = CoroutineExceptionHandler() { _, exception ->
        when(exception){
            is SocketTimeoutException -> catInfo.value = Error(exception.localizedMessage ?: "An error occurred")
            else -> CrashMonitor.trackWarning()
        }
    }

    init {
        loadInfo()
    }

    fun getCatInfo(): LiveData<Result> {
        return catInfo
    }

    private fun loadInfo() {
        viewModelScope.launch(exceptionHandler) {
            val fact = async { handleResponse(catsService.getCatFact()) }
            val image = async { handleResponse(imageService.getRandomImage()) }
            catInfo.value = Success<CatsModel>(CatsModel(fact.await(), image.await()))
        }
    }

    fun onButtonClicked() {
        loadInfo()
    }

}