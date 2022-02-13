package otus.homework.coroutines
import android.util.Log
import kotlinx.coroutines.*


class PresenterScope : CoroutineScope {
    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
        }

    override val coroutineContext = Dispatchers.Main + SupervisorJob() + exceptionHandler
}
