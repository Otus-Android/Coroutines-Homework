package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.network.services.CatsService
import otus.homework.coroutines.network.services.RandomCatImageService
import otus.homework.coroutines.uientities.UiFactEntity
import java.net.SocketTimeoutException

class CatsViewModel(
    private val crashMonitor: CrashAnalyticManager,
    private val catsService: CatsService,
    private val randomCatImageService: RandomCatImageService,
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { ctx, exception ->
        exception.printStackTrace()
        crashMonitor.trackWarning(exception = exception)
        _fact.value = OtherError(errorMessage = exception.localizedMessage)
    }

    private val _fact: MutableLiveData<Result<UiFactEntity>> = MutableLiveData()
    val fact: LiveData<Result<UiFactEntity>> = _fact

    override fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
        super.onCleared()
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            val result = exceptionHandler {
                getFactWithImage()
            }

            _fact.value = result
        }
    }

    fun onStop() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private suspend fun getFactWithImage(): Result<UiFactEntity> = coroutineScope {
        val catFact = async { catsService.getCatFact() }
        val randomCatImage = async { randomCatImageService.getRandomCatImage() }

        val factText = catFact.await().text

        val imageUrl = randomCatImage.await().imageUrl

        val result = UiFactEntity(fact = factText, imageUrl = imageUrl)

        return@coroutineScope Success(result = result)
    }

    private suspend fun <T> exceptionHandler(callback: suspend () -> Result<T>): Result<T> {
        return try {
            callback.invoke()
        } catch (exception: SocketTimeoutException) {
            exception.printStackTrace()
            ServerError
        }
    }
}