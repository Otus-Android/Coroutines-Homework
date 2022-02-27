package otus.homework.coroutines

import kotlinx.coroutines.*

class PresenterScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine")
}