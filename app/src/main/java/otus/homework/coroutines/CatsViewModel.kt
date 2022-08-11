package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val dispatcherIo: CoroutineDispatcher
) : ViewModel() {

    private val _content = MutableLiveData<Event<CatsContent>>()
    val content: LiveData<Event<CatsContent>>
        get() = _content

    private val _showToast = MutableLiveData<Event<String>>()
    val showToast: LiveData<Event<String>>
        get() = _showToast

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
        val errorMessage = t.message ?: "Unknown exception"
        CrashMonitor.trackWarning(message = errorMessage, throwable = t)
    }

    private fun <T> CoroutineScope.catResponseAsync(block: suspend () -> T) =
        async(dispatcherIo) {
            try {
                Result.Success(block())
            } catch (e: SocketTimeoutException) {
                val errorMessage = "Failed to get a response from the server"
                CrashMonitor.trackWarning(message = errorMessage, throwable = e)
                Result.Error(errorMessage)
            }
        }

    private inline fun <reified T> resultHandle(result: Result): T? = when (result) {
        is Result.Success<*> -> result.result as T
        is Result.Error -> {
            _showToast.value = Event(result.message)
            null
        }
    }

    fun refreshContent() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val deferredFact = catResponseAsync { catsService.getCatFact() }
            val deferredImageUrl = catResponseAsync { catsImageService.getImageUrl() }
            val resultFact = deferredFact.await()
            val resultImageUrl = deferredImageUrl.await()
            val fact = resultHandle<Fact>(resultFact)
            val imageUrl = resultHandle<ImageUrl>(resultImageUrl)
            val content = CatsContent(fact = fact, imageUrl = imageUrl)
            _content.value = Event(content)
        }
    }
}