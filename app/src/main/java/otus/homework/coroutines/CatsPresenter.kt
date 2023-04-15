package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.api.CatsImageService
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.models.CatInfoDto
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.PresenterScope
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private val baseScope = PresenterScope()

    fun onInitComplete() {
        baseScope.launch {
            val responseFact = async { catsService.getCatFact() }
            val responseImage = async { catsImageService.getCatImage() }
            try {
                _catsView?.populate(
                    CatInfoDto(
                        text = responseFact.await().body()?.fact,
                        imageUrl = responseImage.await().body()?.get(0)?.url,
                    )
                )
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        throw e
                    }
                    is SocketTimeoutException -> {
                        _catsView?.showToast("Не удалось получить ответ от сервером")
                    }
                    else -> {
                        CrashMonitor.trackWarning(e.message.toString())
                        _catsView?.showToast(e.message.toString())
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onDestroy() {
        baseScope.cancel()
    }
}