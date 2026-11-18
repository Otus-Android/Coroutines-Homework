package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) : ICatsPresenter {

    private val presenterScope = PresenterScope()

    private var _catsView: ICatsView? = null

    var currentJob: Job? = null

    override fun updateFactAndImage() {
        currentJob?.cancel()

        currentJob = presenterScope.launch {
            val result = fetchCatFactWithImage()
            _catsView?.populate(result)
            if (result is Result.Error && result.throwable !is SocketTimeoutException) {
                CrashMonitor.trackWarning(result.throwable)
            }
            currentJob = null
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun stop() {
        presenterScope.cancel()
    }

    private suspend fun getRandomCatFact(): Fact {
        return catsService.getCatFact()
    }

    private suspend fun getCatImageInfo(): CatImage {
        return catsService.getCatImageInfo().first()
    }

    private suspend fun fetchCatFactWithImage(): Result<UiState> {
            val catFact = presenterScope.async {
                getRandomCatFact()
            }
            val catImage = presenterScope.async {
                getCatImageInfo()
            }
            return try {
                Result.Success(
                    UiState(
                        catFact.await().fact,
                        catImage.await().url
                    )
                )
            } catch (th: Throwable) {
                if (th is CancellationException) throw th
                Result.Error(th)
            }

    }

    private class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext =
            SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}

interface ICatsPresenter {
    fun updateFactAndImage()
}