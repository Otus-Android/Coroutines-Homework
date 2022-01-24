package otus.homework.coroutines.other

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class PresenterScope(context: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext = context
}