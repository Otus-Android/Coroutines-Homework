package otus.homework.coroutines
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine") +  SupervisorJob()
}