package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class PresenterScope(override val coroutineContext: CoroutineContext) : CoroutineScope