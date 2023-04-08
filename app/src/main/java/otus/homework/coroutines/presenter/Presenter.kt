package otus.homework.coroutines.presenter

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface Presenter {
    val scope: CoroutineScope
        get() = PresenterScope()
}

class PresenterScope: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
}


