package otus.homework.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PresenterScope(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val coroutineName: CoroutineName = CoroutineName("CatsCoroutine")
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = coroutineDispatcher + coroutineName
}