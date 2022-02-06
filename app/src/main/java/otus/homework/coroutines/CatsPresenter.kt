package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            val factJob = async(SupervisorJob()) { catsService.getCatFact() }
            val imageJob = async(SupervisorJob()) { catsService.getCatImage() }

            try {
                _catsView?.populate(
                    FactAndImage(
                        factJob.await(),
                        imageJob.await()
                    )
                )
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.onError("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        e.message?.let { _catsView?.onError(it) }
                        CrashMonitor.trackWarning()
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}