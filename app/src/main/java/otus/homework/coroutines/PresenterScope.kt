package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal class PresenterScope : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + CoroutineName("CatsCoroutine")
}