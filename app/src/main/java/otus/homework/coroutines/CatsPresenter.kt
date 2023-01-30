package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picsService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private val myScope = PresenterScope()

    fun onInitComplete() {
        myScope.launch {
            try {
                // delay для проверки работы myScope.cancel()
                //delay(2000)

                val fact = async {
                    catsService.getCatFact()
                }
                val pic = async {
                    picsService.getPic()
                }

                //throw SocketTimeoutException()
                //throw Exception("exception")
                _catsView?.populate(Pair(fact.await(), pic.await()))
            } catch (e: SocketTimeoutException) {
                _catsView?.showToastFromRes(R.string.failed_response)
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(e.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        myScope.cancel()
        _catsView = null
    }
}