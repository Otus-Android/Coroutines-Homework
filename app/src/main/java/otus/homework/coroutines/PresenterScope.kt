package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

private const val COROUTINE_NAME = "CatsCoroutine"

class PresenterScope(override val coroutineContext: CoroutineContext) : CoroutineScope{
    companion object {
        val defaultScope = PresenterScope(
            CoroutineName(COROUTINE_NAME)
                    + Dispatchers.Main +
                    SupervisorJob()
        )
    }
}