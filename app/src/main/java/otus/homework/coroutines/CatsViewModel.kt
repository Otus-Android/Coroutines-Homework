package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
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

    private fun <T> catResponseAsync(
        scope: CoroutineScope,
        responseBlock: suspend () -> Response<T>
    ): Deferred<Result> = scope.async(dispatcherIo) {
        try {
            val response = responseBlock()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Result.Success(response.body())
                } else {
                    val crashMessage = "Empty response body"
                    CrashMonitor.trackWarning(message = crashMessage)
                    Result.Error(crashMessage)
                }
            } else {
                val crashMessage = response.errorBody().toString()
                CrashMonitor.trackWarning(message = crashMessage)
                Result.Error(crashMessage)
            }
        } catch (e: Exception) {
            val errorMessage: String
            when (e) {
                is CancellationException -> throw e
                is SocketTimeoutException -> {
                    errorMessage = "Failed to get a response from the server"
                    CrashMonitor.trackWarning(message = e.message, throwable = e)
                }
                else -> {
                    errorMessage = e.message ?: "Unknown exception"
                    CrashMonitor.trackWarning(message = e.message, throwable = e)
                }
            }
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
        viewModelScope.launch {
            val deferredFact = catResponseAsync(this) { catsService.getCatFact() }
            val deferredImageUrl = catResponseAsync(this) { catsImageService.getImageUrl() }
            val resultFact = deferredFact.await()
            val resultImageUrl = deferredImageUrl.await()
            val fact = resultHandle<Fact>(resultFact)
            val imageUrl = resultHandle<ImageUrl>(resultImageUrl)
            val content = CatsContent(fact = fact, imageUrl = imageUrl)
            _content.value = Event(content)
        }
    }
}