package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

object CatsCoroutineScope : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
}