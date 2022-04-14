package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName(COROUTINE_NAME)
}