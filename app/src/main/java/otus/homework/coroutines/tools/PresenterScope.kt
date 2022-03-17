package otus.homework.coroutines.tools

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope() : CoroutineScope {

    private val handler = CoroutineExceptionHandler(handler = { _, exception ->
        CrashMonitor.trackError(exception.message.toString())
    })

    override val coroutineContext: CoroutineContext
        get() = CoroutineName("CatsCoroutine") + Job() + Dispatchers.Main + handler
}