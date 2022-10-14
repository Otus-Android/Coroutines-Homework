package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + SupervisorJob() + coroutineExceptionHandler + CoroutineName("CatsCoroutine")
}