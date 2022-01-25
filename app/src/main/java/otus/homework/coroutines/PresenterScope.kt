package otus.homework.coroutines

import kotlinx.coroutines.*

object PresenterScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine")
}