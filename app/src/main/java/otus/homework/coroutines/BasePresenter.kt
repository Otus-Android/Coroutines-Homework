package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

open class BasePresenter<T> {
    protected var view: T? = null

    val presenterScope: CoroutineScope
        get() = CloseableCoroutineScope(
            SupervisorJob()
                + Dispatchers.Main.immediate
                + CoroutineName("CatsCoroutine")
        )

    fun attachView(attachView: T) {
        view = attachView
    }

    fun detachView() {
        view = null
        (presenterScope as? Closeable)?.close()
    }
}

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}