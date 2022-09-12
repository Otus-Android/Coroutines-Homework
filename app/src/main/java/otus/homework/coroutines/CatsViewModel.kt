package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.random.Random

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
) : ViewModel() {

    private val _event = MutableLiveData<Event<Any>>()
    val event: LiveData<Event<Any>>
        get() = _event

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
        val errorMessage = t.message ?: "Unknown exception"
        CrashMonitor.trackWarning(message = errorMessage, throwable = t)
    }

    private suspend fun <T> catResponse(block: suspend () -> T) =
        try {
            Result.Success(block())
        } catch (e: SocketTimeoutException) {
            val errorMessage = "Failed to get a response from the server"
            CrashMonitor.trackWarning(message = errorMessage, throwable = e)
            Result.Error(errorMessage)
        }

    private inline fun <reified T> resultHandle(result: Result): T? = when (result) {
        is Result.Success<*> -> result.result as T
        is Result.Error -> {
            _event.value = Event(result.message)
            null
        }
    }

    fun refreshContent() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val deferredFact = async { catResponse { catsService.getCatFact() } }
            val deferredImageUrl = async { catResponse { catsImageService.getImageUrl() } }
            val resultFact = deferredFact.await()
            val resultImageUrl = deferredImageUrl.await()
            val fact = resultHandle<Fact>(resultFact)
            val imageUrl = resultHandle<ImageUrl>(resultImageUrl)
            val content = CatsContent(fact = fact, imageUrl = imageUrl)
            _event.value = Event(content)
        }
    }
}