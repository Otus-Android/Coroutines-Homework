package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineName("CatsCoroutine")
}