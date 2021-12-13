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
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

        fun onInitComplete() {
            presenterScope.launch {
                supervisorScope {
                    val defFact = async(Dispatchers.IO){factsService.getCatFact()}
                    val defPic = async(Dispatchers.IO) { picsService.getCatPic() }
                    try {
                        _catsView?.populate(FactAndPicture(defFact.await(), defPic.await()))
                    } catch (e: SocketTimeoutException) {
                        _catsView?.showToastMsg(R.string.server_error)
                    } catch(e: CancellationException){
                        throw e
                    } catch (e: Exception){
                        CrashMonitor.trackWarning()
                        e.message?.also {
                            _catsView?.showToastMsg(it)
                        }
                    }
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

