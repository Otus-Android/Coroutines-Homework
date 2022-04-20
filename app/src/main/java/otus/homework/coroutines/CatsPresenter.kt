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

            val fact = Fact("", false, "","", "", true, "", "","")
            try {

                val job1 = async {

                        val response = catsService.getCatFact()
                        if (response.isSuccessful && response.body() != null) {
                            fact.text = response.body()!!.text
                        }
                        else _catsView?.toast("Сервис фактов отвечает неправильно")
                }

                val job2 = async {
                        val responseView = catsViewService.getCatView()
                        if (responseView.isSuccessful && responseView.body() != null) {
                            fact.source = responseView.body()!!.file
                        } else _catsView?.toast("Сервис картинок отвечает неправильно")
                }
                job1.await()
                job2.await()
            } catch (error: SocketTimeoutException) {
                _catsView?.toast("Не удалось получить ответ от сервера")
            } catch (error: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.toast(error.message.toString())
            }
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