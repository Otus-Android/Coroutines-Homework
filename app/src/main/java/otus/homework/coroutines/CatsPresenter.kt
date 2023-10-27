package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.CatFact
import otus.homework.coroutines.models.CatImage
import otus.homework.coroutines.models.CatUi
import java.net.SocketTimeoutException

private val catsPresenterCoroutineScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService,
    private val crashMonitor: CrashMonitor,
) {
    private val presenterScope: CoroutineScope = catsPresenterCoroutineScope
    private var _catsView: ICatsView? = null
    private var requestJob: Job? = null

    fun onInitComplete() {
        if (requestJob?.isActive == true) {
            crashMonitor.trackWarning("the request is already being made")
            return
        }
        requestJob = presenterScope.launch {
            val catFactRequest = async(Dispatchers.IO) { getCatsFact() }
            val catImagesRequest = async(Dispatchers.IO) { getCatsImages() }
            collectCatsInfo(catFactRequest.await(), catImagesRequest.await())
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        requestJob?.cancel()
        _catsView = null
    }

    private suspend fun collectCatsInfo(
        catsFactResult: Result<CatFact>,
        catsImageResult: Result<CatImage>,
    ) {
        catsFactResult
            .onSuccess { catFact ->
                catsImageResult
                    .onSuccess { catImage ->
                        val catUi = CatUi(
                            fact = catFact.text,
                            imageUrl = catImage.url
                        )
                        _catsView?.populate(catUi)
                    }
                    .onFailure(::handleError)
            }
            .onFailure(::handleError)
    }

    private suspend fun getCatsFact(): Result<CatFact> {
        return try {
            Result.success(catsFactService.getCatFact())
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    private suspend fun getCatsImages(): Result<CatImage> {
        return try {
            Result.success(catsImageService.getCatsImages().first())
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    private fun handleError(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException -> {
                crashMonitor.trackError("error getting facts", ex)
                _catsView?.showError("Не удалось получить ответ от сервера")
            }

            else -> {
                crashMonitor.trackError("error getting facts", ex)
                _catsView?.showError(ex.message ?: "Произошла неизвестная ошибка")
            }
        }
    }
}