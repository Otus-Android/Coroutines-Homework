package otus.homework.coroutines

import kotlinx.coroutines.*

class PresenterScope : CoroutineScope {
    private val name = "CatsCoroutine"
    override val coroutineContext = SupervisorJob() + Dispatchers.Main + CoroutineName(name)
}
