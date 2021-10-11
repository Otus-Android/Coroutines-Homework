package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope(context: CoroutineContext) : CoroutineScope {

    override val coroutineContext: CoroutineContext = context
}

val presenterScope: PresenterScope =
    PresenterScope(SupervisorJob() + Dispatchers.Main.immediate + CoroutineName("CatsCoroutine"))