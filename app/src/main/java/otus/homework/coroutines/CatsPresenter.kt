package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Default)

    private val getCatsJob =
        presenterScope.launch(CoroutineName("CatsCoroutine")) {
            withContext(Dispatchers.Main) {
                try {
                    flowOf(catsService.getCatFact()).collectLatest {
                        _catsView?.populate(/*(it as Result.Success).value*/it)
                    }

                } catch (es: Exception) {
                    val message = when (es) {
                        is SocketTimeoutException -> "Не удалось получить ответ от сервером"
                        else -> {
                            CrashMonitor.trackWarning()
                            es.message
                        }
                    }
                    _catsView?.showMessage(message.toString())
                }
            }
        }

    fun onInitComplete() = presenterScope.launch {
        getCatsJob.start()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        getCatsJob.cancel()
    }
}