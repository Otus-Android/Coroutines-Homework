package otus.homework.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import otus.homework.coroutines.network.facts.base.image.ImageService
import otus.homework.coroutines.network.facts.base.AbsCatService
import otus.homework.coroutines.network.facts.base.CatData
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: AbsCatService,
    private val catsImageService: ImageService
) {
    private var _catsView: ICatsView? = null
    private val _presenterScope = PresenterScope()

    private lateinit var catFactJob: Job
    private lateinit var catImageJob: Job

    private val catData = CatData()

    fun onInitComplete() {
        catFactJob = _presenterScope.launch {
            try {
                val catFact = catsService.getCatFact()
                catData.fact = catFact
                if (catImageJob.isCompleted) populateView()
            } catch (exp: Exception) {
                if (exp is SocketTimeoutException) {
                    _catsView?.showError(R.string.socket_timeout_exception_error_text)
                } else {
                    CrashMonitor.trackWarning()
                    _catsView?.showError(exp.message)
                }
            }
        }

        catImageJob = _presenterScope.launch {
            try {
                val imageUrl = catsImageService.getCatImageUrl()
                catData.imageUrl = imageUrl
                if (catFactJob.isCompleted) populateView()
            } catch (exp: Exception) {
                if (exp is SocketTimeoutException) {
                    _catsView?.showError(R.string.socket_timeout_exception_error_text)
                } else {
                    CrashMonitor.trackWarning()
                    _catsView?.showError(exp.message)
                }
            }
        }
    }

    private fun populateView() {
        _catsView?.populate(catData)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun stopCatJob() {
        catFactJob.cancel()
        catImageJob.cancel()
    }
}