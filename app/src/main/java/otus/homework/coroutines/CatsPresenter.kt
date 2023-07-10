package otus.homework.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imgService: ImgService
) {

    private val scope = PresenterScope()
    private var _catsView: ICatsView? = null

    private var catsDeferred: Deferred<String>? = null
    private var imgDeferred: Deferred<String>? = null

    fun onInitComplete() {

        scope.launch {
            try {
                catsDeferred?.cancel()
                imgDeferred?.cancel()

                catsDeferred = scope.async {
                    catsService.getCatFact().fact
                }
                catsDeferred!!.start()

                imgDeferred = scope.async {
                    imgService.getRandomImg().message
                }
                imgDeferred!!.start()

                val fact = PresentationFact(catsDeferred!!.await(), imgDeferred!!.await())
                _catsView?.populate(fact)

            } catch (e: SocketTimeoutException) {
                CrashMonitor.trackWarning()
                _catsView?.showError("Не удалось получить ответ от сервером")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catsDeferred?.cancel()
        imgDeferred?.cancel()
        _catsView = null
    }
}