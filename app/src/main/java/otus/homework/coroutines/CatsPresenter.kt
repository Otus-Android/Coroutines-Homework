package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: CoroutineScope,
) {

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("myTag", exception.toString())
    }

    fun onInitComplete() {
        presenterScope.launch(handler) {
            try {
                val response = catsService.getCatFact()
                if (response.isSuccessful && response.body() != null) {
                    val listOfFacts = response.body()!!
                    _catsView?.populate(listOfFacts[0])
                }
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> _catsView?.showToast(R.string.timeout_message)
                    else -> {
                        _catsView?.showToast(e.message)
                        CrashMonitor.trackWarning()
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
    }
}