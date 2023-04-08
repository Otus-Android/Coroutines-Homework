package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

object PresenterScope: CoroutineScope { // холдер для CoroutineContext
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
    // SupervisorJob() вместо Job(), чтобы ошибки в одной дочерней корутине не привели к
    // остановке всех корутин в скоупе
}