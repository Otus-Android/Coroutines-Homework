package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PresenterScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine")
}