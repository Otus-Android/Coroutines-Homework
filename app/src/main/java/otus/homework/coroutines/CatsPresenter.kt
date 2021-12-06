package otus.homework.coroutines

import android.util.Log
import android.util.Log.ERROR
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.*
import otus.homework.coroutines.model.CatFact
import otus.homework.coroutines.model.CatModel
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    var job: Job? = null
    var presenterScope = PresenterScope()

    fun onInitComplete() {
        job = presenterScope.launch {
            supervisorScope {
                try {
                    var catFactDef = async(Dispatchers.IO) { catsService.getCatFact() }
                    var catImageDef = async(Dispatchers.IO) {catsService.getCatImage()}
                    val catModel = CatModel(catFactDef.await(), catImageDef.await())
                    val model: CatFact = catFactDef.await()
                    _catsView?.populate(catModel)
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> {
                            _catsView?.toast("Не удалось получить ответ от сервера")
                        }
                        is CancellationException -> {
                            throw e
                        }
                        else -> {
                            CrashMonitor.trackWarning(e.message!!)
                            _catsView?.toast(e.message!!)
                        }
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