package otus.homework.coroutines

import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val factsService: FactsService,
    private val picsService: PicsService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            supervisorScope {
                val facts = async(Dispatchers.IO) {
                    factsService.getCatFact()
                }
                val pics = async(Dispatchers.IO) {
                    picsService.getCatPicture()
                }
                try {
                    _catsView?.populate(CatModel(facts.await(), pics.await()))
                } catch (ex: SocketTimeoutException) {
                    _catsView?.showToast(R.string.socket_error)
                } catch(ex: CancellationException){
                    throw ex
                } catch (ex: Throwable) {
                    CrashMonitor.trackWarning()
                    if (ex.message != null) {
                        _catsView?.showToast(ex.message!!)
                    } else {
                        _catsView?.showToast(R.string.general_error)
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.coroutineContext.cancel()
        _catsView = null
    }
}