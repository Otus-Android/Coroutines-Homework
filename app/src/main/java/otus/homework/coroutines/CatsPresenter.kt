package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    //private val picsService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private val myScope = PresenterScope()

    fun onInitComplete() {
        myScope.launch {
            try {
                // delay для проверки работы myScope.cancel()
                //delay(2000)

                val factResponse = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val picResponse = async(Dispatchers.IO) {
                    catsService.getPic(DiContainer.picUrl)
                }

                val fact = factResponse.await()
                val pic = picResponse.await()

                if (fact.body() != null && pic.body() != null) {
                    _catsView?.populate(Pair(fact.body()!!, pic.body()!!))
                }

                //throw Exception("exception")
            } catch (e: Exception) {
                // Тут выкидывает JobCancellationException, но в cause есть SocketTimeoutException
                // Поэтому сделал проверку на оба случая
                if (e.cause is SocketTimeoutException || e is SocketTimeoutException) {
                    _catsView?.showToastFromRes(R.string.failed_response)
                } else {
                    CrashMonitor.trackWarning()
                    _catsView?.showToast(e.message)
                }
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