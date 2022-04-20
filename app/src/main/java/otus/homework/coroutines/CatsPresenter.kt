package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main+ CoroutineName("CatsCoroutine"))

    fun onInitComplete() {

        presenterScope.launch {

            try {
                val response = catsService.getCatFact()
                if (response.isSuccessful && response.body() != null) {
                    _catsView?.populate(response.body()!!)
                }
            }
            catch(error: SocketTimeoutException){
                _catsView?.toast("Не удалось получить ответ от сервера")
            }
            catch(error: Exception){
                CrashMonitor.trackWarning()
                _catsView?.toast(error.message.toString())
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