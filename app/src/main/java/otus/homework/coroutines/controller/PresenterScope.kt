package otus.homework.coroutines.controller

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PresenterScope : CoroutineScope {
    private val name = "CatsCoroutine"
    override val coroutineContext = SupervisorJob() + Dispatchers.Main + CoroutineName(name)
}
