package otus.homework.coroutines.utils.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * `Custom CoroutineScope` с `main dispatcher-ом` и заданным `CoroutineName`
 *
 * @param dispatcher обертка получения `coroutine dispatcher`
 */
class PresenterScope(private val dispatcher: Dispatcher) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatcher.main + CoroutineName("CatsCoroutine")

}