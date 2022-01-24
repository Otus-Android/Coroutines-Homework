package otus.homework.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

class PresenterScope(
    private val dispatcher: CoroutineDispatcher,
    private val name: CoroutineName
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = dispatcher + name
}