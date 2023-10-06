package otus.homework.coroutines.presentation

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CatsRepository
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.data.Result
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsRepository,
) {
    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")
    )
    private var view: ICatsView? = null

    fun onInitComplete() {
        coroutineScope.launch {
            val factDeferred = async(Dispatchers.IO) {
                catsService.getCatFact()
            }

            when (val factResult = factDeferred.await()) {
                is Result.Success -> {
                    view?.populate(factResult.model)
                }
                is Result.Error -> {
                    when(val exception = factResult.exception) {
                        is SocketTimeoutException -> {
                            view?.showErrorToast(CatsService.TIMEOUT_MESSAGE)
                        }
                        else -> {
                            exception.message?.let { view?.showErrorToast(it) }
                            CrashMonitor.trackWarning()
                        }
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        view = catsView
    }

    fun detachView() {
        view = null
    }
}