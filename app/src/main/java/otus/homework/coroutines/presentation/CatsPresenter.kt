package otus.homework.coroutines.presentation

import kotlinx.coroutines.*
import otus.homework.coroutines.*
import otus.homework.coroutines.data.CatModel
import otus.homework.coroutines.presentation.view.ICatsView
import otus.homework.coroutines.service.CrashMonitor
import otus.homework.coroutines.service.FactsService
import otus.homework.coroutines.service.PicsService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val factsService: FactsService,
    private val picsService: PicsService
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
//                    throw SocketTimeoutException()
//                    throw IllegalArgumentException("TEST")
                } catch (ex: SocketTimeoutException) {
                    _catsView?.showToast(msgId = R.string.socket_error)
                } catch(ex: CancellationException){
                    throw ex
                } catch (ex: Throwable) {
                    CrashMonitor.trackWarning(ex)
                    if (ex.message != null) {
                        _catsView?.showToast(ex.message!!)
                    } else {
                        _catsView?.showToast(msgId = R.string.general_error)
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