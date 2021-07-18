package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsFactService: CatsService,
    private val catsImageService: CatsImageService
) {

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factImageDeferred = async(Dispatchers.IO) { catsImageService.getCatImage() }
                val factsDeferred = async(Dispatchers.IO) { catsFactService.getCatFact() }
                val factImage = factImageDeferred.await().file
                val facts = factsDeferred.await()
                val fact = if (facts.isEmpty()) throw IllegalArgumentException("empty fact") else facts[0].fact

                _catsView?.populate(Result.Success(PresentationFact(fact, factImage)))
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> _catsView?.showError(Result.Error(null))
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showError(Result.Error(e.message))
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