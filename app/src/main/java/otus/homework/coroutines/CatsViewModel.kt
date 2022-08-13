package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {
    private val hideViewsChannel = Channel<Result.Success>(1)
    private val populateChannel = Channel<Result.Success>(1)
    private val progressDialogChannel = Channel<Result.Success>(1)
    private val toastChannel = Channel<Result.Failure>(1)

    fun onInitComplete() {
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            progressDialogChannel.send(Result.Success(true))
            hideViewsChannel.send(Result.Success(true))
            val fact = catsService.getCatFact(FACT_URI)
            val imageUri = catsImageService.getCatImage(IMAGE_URI)
            populateChannel.send(Result.Success(Pair(fact, imageUri)))
        }
    }

    internal fun getViewsChannelFlow() = hideViewsChannel.receiveAsFlow()
    internal fun getPopulateChannel() = populateChannel.receiveAsFlow()
    internal fun getProgressDialogsFlow() = progressDialogChannel.receiveAsFlow()
    internal fun getToastChannelFlow() = toastChannel.receiveAsFlow()

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                CrashMonitor.trackWarning()
                if (exception == java.net.SocketTimeoutException()) {
                    toastChannel.send(Result.Failure("Не удалось получить ответ от сервером"))
                } else {
                    CrashMonitor.trackWarning()
                    exception.message?.let { toastChannel.send(Result.Failure(it)) }
                }
            }
        }
    }

    companion object {
        private const val FACT_URI = "https://cat-fact.herokuapp.com/facts/random?animal_type=cat"
        private const val IMAGE_URI = "https://aws.random.cat/meow"
    }
}