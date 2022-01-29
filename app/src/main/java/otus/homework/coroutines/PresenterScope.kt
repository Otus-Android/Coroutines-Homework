package otus.homework.coroutines
import android.util.Log
import kotlinx.coroutines.*


private val exceptionHandler =
    CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
    }

class PresenterScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + SupervisorJob() + exceptionHandler
}

