package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class PresenterScope(private val coroutineName: String = "PresenterCoroutine") : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main + CoroutineName(coroutineName)
}