package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsViewService: CatsViewService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main+ CoroutineName("CatsCoroutine"))

    fun onInitComplete() {

        presenterScope.launch {
            var text = ""
            var source = ""
            try {

                text = async {
                    catsService.getCatFact().text
                }.await()

                source = async {
                    catsViewService.getCatView().file
                }.await()

            } catch (error: SocketTimeoutException) {
                _catsView?.toast("Не удалось получить ответ от сервера")
            } catch (error: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.toast(error.message.toString())
            }
            val fact = FactShort(text=text, source = source)
            _catsView?.populate(fact)
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