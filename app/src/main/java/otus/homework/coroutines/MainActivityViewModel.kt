package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import kotlin.coroutines.coroutineContext


class MainActivityViewModel(private val catsService: CatsService) : ViewModel() {
    private val _catsData = MutableLiveData<Result<CustomCatPresentationModel>>()

    val catsData: LiveData<Result<CustomCatPresentationModel>>
        get() = _catsData

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        if (e is SocketTimeoutException) _catsData.postValue(Result.Error(e))
        else CrashMonitor.trackWarning(e.message)
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            getData()
        }
    }

    private suspend fun getData() {
        val fact = withContext(coroutineContext + exceptionHandler) {
            catsService.getCatFact()
        }
        val image = withContext(coroutineContext + exceptionHandler) {
            catsService.getCatImage()
        }

        val data = CustomCatPresentationModel(
            checkNotNull(fact.body())[0].fact,
            checkNotNull(image.body()).file
        )
        _catsData.postValue(Result.Success(data))
    }
}