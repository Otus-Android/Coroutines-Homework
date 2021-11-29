package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class HomeWorkScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine")
}

