package otus.homework.coroutines

import android.widget.Toast
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

class CatsPresenter(
    private val catsService: CatsService

) {
    private var _catsView: ICatsView? = null
    private var job: Job? = null

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
        }

    fun onInitComplete() {
        getCatsFact()
    }

    fun getCatsFact() {
        var picture: Picture? = null
        var fact: Fact? = null
        val scope = PresenterScope()

        job = scope.launch() {
            try {
                coroutineScope {
                    var one = async {
                        fact = catsService.getCatFact()
                    }
                    var two = async {
                        picture = catsService.getCatPicture()
                    }

                    awaitAll(one, two)
                    _catsView?.populate(FactAndPicture(fact!!, picture!!))

                }
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.showError("Ошибка сокета")
            }
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        if (job?.isActive == true) {
            job?.cancel()
        }
        _catsView = null
    }

}