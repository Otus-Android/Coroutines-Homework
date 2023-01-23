package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private const val COROUTINE_NAME = "CatsCoroutine"

class PresenterScope(override val coroutineContext: CoroutineContext) : CoroutineScope{
    companion object {
        private val handler = CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }

        fun defaultScope() = PresenterScope(
            CoroutineName(COROUTINE_NAME)
                    + Dispatchers.Main
                    + SupervisorJob()
                    + handler
        )
    }
}