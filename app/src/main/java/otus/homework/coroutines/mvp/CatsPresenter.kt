package otus.homework.coroutines.mvp

import android.util.Log
import kotlinx.coroutines.*
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.data.CatDataModel
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.utils.CrashMonitor
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope: CoroutineScope = CoroutineScope(
        Job() +
                Dispatchers.Main +
                CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        presenterScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e("throwable", throwable.toString())
            CrashMonitor.trackWarning()
        }) {
            try {
                cancel()
                val fact = this.async { catsService.getCatFact() }
                val picUrl = this.async { catsService.getPicUrl() }
                _catsView?.populate(CatDataModel(fact.await(), picUrl.await()))
            } catch (e: SocketTimeoutException) {
                _catsView?.showSocketTimeoutException()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

}