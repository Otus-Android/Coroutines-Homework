package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class PresenterScope: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = CoroutineName( "CatsCoroutine") +
                Dispatchers.Main +
                SupervisorJob() +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    print("got $throwable in handler")
                }
}