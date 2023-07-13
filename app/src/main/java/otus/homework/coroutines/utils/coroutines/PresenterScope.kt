package otus.homework.coroutines.utils.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class PresenterScope(private val dispatcher: Dispatcher) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatcher.main + CoroutineName("CatsCoroutine")

}