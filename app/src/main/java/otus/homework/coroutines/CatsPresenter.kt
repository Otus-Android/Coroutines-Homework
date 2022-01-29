package otus.homework.coroutines



import android.widget.Toast
import kotlinx.coroutines.*

import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService

) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        getCatsFact()
    }

    fun getCatsFact() {
        val scope = PresenterScope()
        job = scope.launch(Dispatchers.Main + CoroutineName("v1coroutine")) {
            try {
                coroutineScope {
                    var one = async {

                        val response = catsService.getCatFact()
                        _catsView?.populate(response!!)
                        throw IllegalAccessException("mess")

                    }
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