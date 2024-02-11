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
    class MyScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")

    }

    private var _catsView: ICatsView? = null
    private var job: Job? = null
    fun onInitComplete() {
        job = MyScope().launch {
                try {
                    val img= async { catsService.getCatFactImg().first()}
                    val fact  = async {  catsService.getCatFact()}
                    _catsView?.populate(General(fact.await(), img.await()))
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> _catsView?.showError("Не удалось получить ответ от сервером")
                        else -> {
                            CrashMonitor.trackWarning(e.message.toString())
                            _catsView?.showError(e.message.toString())
                        }
                    }
                }
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