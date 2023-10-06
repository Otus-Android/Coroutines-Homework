package otus.homework.coroutines.presentation

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.domain.repository.ImageUrlRepository

class CatsPresenter(
    private val catsService: FactRepository,
    private val imageUrlRepository: ImageUrlRepository,
) {
    private var catFactJob: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        coroutineScope.launch(Dispatchers.Main) {
            view?.populate(ScreenState.Error(exception.message))
        }
        CrashMonitor.trackWarning(exception)
    }

    private val coroutineScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineName("CatsCoroutine")
    )
    private var view: ICatsView? = null

    fun onInitComplete() {
        view?.populate(ScreenState.Loading)
        catFactJob?.cancel()
        catFactJob = coroutineScope.launch(exceptionHandler) {
            val factDeferred = async(Dispatchers.IO) { catsService.getCatFact() }
            val imageUrlDeferred = async(Dispatchers.IO) { imageUrlRepository.getImageUrl() }

            if (isActive) {
                val factResult = factDeferred.await()
                val urlResult = imageUrlDeferred.await()

                withContext(Dispatchers.Main) {
                    if (factResult is Result.Success && urlResult is Result.Success) {
                        view?.populate(
                            ScreenState.Model(
                                text = factResult.model.text,
                                photoUrl = urlResult.model.url,
                            )
                        )
                    } else {
                        view?.populate(ScreenState.TimeoutException)
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        view = catsView
    }

    fun detachView() {
        view = null
        catFactJob?.cancel()
    }
}