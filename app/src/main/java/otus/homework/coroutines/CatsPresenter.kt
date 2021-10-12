package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val factsService: FactsService,
    private val picsService: PicsService
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null
    private fun PresenterScope() = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    fun onInitComplete() {
        job = PresenterScope().launch {
            try {
                val defFact = async(CoroutineScope(Dispatchers.IO).coroutineContext){factsService.getCatFact()}
                val defPic = async(CoroutineScope(Dispatchers.IO).coroutineContext) { picsService.getCatPic() }
                _catsView?.populate(FactAndPicture(defFact.await(), defPic.await()))

            } catch (e: SocketTimeoutException){
                _catsView?.showToastMsg(R.string.server_error)
            } catch(e: CancellationException) {

            } catch (e: Exception){
                CrashMonitor.trackWarning()
                e.message?.also {
                    _catsView?.showToastMsg(it)
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }
}
