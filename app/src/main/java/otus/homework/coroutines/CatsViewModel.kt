package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val timeoutErrorMessage: String
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<Result<CatPresentation>>()
    val catsLiveData: LiveData<Result<CatPresentation>> = _catsLiveData

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
        val error = when (exception) {
            is SocketTimeoutException -> Exception(timeoutErrorMessage)
            is CancellationException -> exception
            else -> exception as? Exception ?: Exception(exception.message)
        }
        _catsLiveData.postValue(Result.Error(error))
    }

    fun loadCatData() {
        viewModelScope.launch(exceptionHandler + CoroutineName("CatsCoroutine")) {
            val factDeferred = async { catsService.getCatFact() }
            val imageDeferred = async { catsImageService.getRandomCatImage() }

            val fact = factDeferred.await()
            val images = imageDeferred.await()

            val imageUrl = images.firstOrNull()?.url
            val catPresentation = CatPresentation(
                fact = fact.fact,
                imageUrl = imageUrl ?: ""
            )

            _catsLiveData.value = Result.Success(catPresentation)
        }
    }
}