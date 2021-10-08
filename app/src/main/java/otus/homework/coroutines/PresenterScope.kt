package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PresenterScope: CoroutineScope {

    override val coroutineContext = SupervisorJob() +
        Dispatchers.Main +
        CoroutineName(coroutineName)

    companion object {
        private const val coroutineName = "CatsCoroutine"
    }
}