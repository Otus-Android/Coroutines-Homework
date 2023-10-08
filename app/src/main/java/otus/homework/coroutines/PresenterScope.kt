package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    private val coroutineName = CoroutineName("CatsCoroutine")
    override val coroutineContext: CoroutineContext = Dispatchers.IO + coroutineName
}