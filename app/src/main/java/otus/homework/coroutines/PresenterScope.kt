package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable)
        }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() + exceptionHandler + CoroutineName("CatsCoroutine")
}