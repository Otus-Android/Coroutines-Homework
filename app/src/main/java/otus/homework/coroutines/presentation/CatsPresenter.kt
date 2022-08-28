package otus.homework.coroutines.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.network.CatImageService
import otus.homework.coroutines.network.CatsService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        _catsView?.showUILoading(true)

        scope.launch {
            supervisorScope {
                try {
                    val catFactDeferred = async() { catsService.getCatFact() }
                    val catImageDeferred = async() { catImageService.getCatImage() }

                    val fact = catFactDeferred.await()
                    val image = catImageDeferred.await()

                    val catModel = CatModel(fact, image)
                    _catsView?.populate(catModel)

                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> {
                            _catsView?.showError("Не удалось получить ответ от сервера")
                        }
                        is RuntimeException -> {
                            Result.Error(e.message)
                        }
                        else -> {
                            CrashMonitor.trackWarning()
                            Result.Error(null)
                        }
                    }
                } finally {
                    _catsView?.showUILoading(false)
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}