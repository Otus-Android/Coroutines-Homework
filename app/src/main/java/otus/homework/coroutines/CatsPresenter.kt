package otus.homework.coroutines

import android.support.annotation.RestrictTo.Scope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {
    inner class MyScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")

    }

    private var _catsView: ICatsView? = null
    private var job: Job? = null
    fun onInitComplete() {
        job = MyScope().launch {

            val fact = async {
                return@async try {
                    catsService.getCatFact()
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> _catsView?.showError("Не удалось получить ответ от сервером")
                        else -> {
                            CrashMonitor.trackWarning(e.message.toString())
                            _catsView?.showError(e.message.toString())
                        }
                    }
                } as Fact
            }.await()
            val img = async {
                return@async try {
                    catsService.getCatFactImg().first()
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> _catsView?.showError("Не удалось получить ответ от сервером")
                        else -> {
                            CrashMonitor.trackWarning(e.message.toString())
                            _catsView?.showError(e.message.toString())
                        }
                    }
                } as Img
            }.await()

            _catsView?.populate(fact, img)
        }

    }
        fun attachView(catsView: ICatsView) {
            _catsView = catsView
        }

        fun detachView() {
            _catsView = null
            job?.cancel()
        }

    }