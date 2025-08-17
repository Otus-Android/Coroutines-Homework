package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    private val scope = CoroutineScope(
        Dispatchers.Main.immediate + SupervisorJob() + CoroutineName("CatsCoroutine")
    )
    private var _catsView: ICatsView? = null


    /**
     * Не понял по условию задачи, что нужно делать, если в одном из запросов ошибка,
     * а вдругом нет и на чьи ошибки надо показывать тосты. Тут тосты показываются только на ошибки
     * в запросе фактов, и ошибка в одном из запросов не отменяет другой
     *
     * Во вьюмоделе сделаю, чтоб ошибка в любом из запросов отменяет оба запроса и ошибки из любого
     * выводятся в текстовое поле
     */
    fun onInitComplete() {
        scope.coroutineContext.cancelChildren()
        val factJob = scope.async(Dispatchers.IO) {
            try {
                val response = catsService.getCatFact()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
                when (e) {
                    is SocketTimeoutException -> {
                        withContext(Dispatchers.Main) {
                            _catsView?.showToast("Не удалось получить ответ от сервера")
                        }
                    }

                    else -> {
                        CrashMonitor.trackWarning()
                        withContext(Dispatchers.Main) {
                            _catsView?.showToast(e.message)
                        }
                    }
                }
                null
            }
        }

        val imageJob = scope.async(Dispatchers.IO) {
            try {
                val response = catsService.getRandomImage()
                if (response.isSuccessful) {
                    response.body()?.firstOrNull()
                } else {
                    null
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
                null
            }
        }

        scope.launch {
            val fact = factJob.await()
            val image = imageJob.await()
            _catsView?.populate(CatsUiModel(fact?.fact, image?.url))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.coroutineContext.cancelChildren()
    }
}