package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import otus.homework.coroutines.Result
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) : ICatsPresenter {

    private val coroutineScope = PresenterScope()

    private var _catsView: ICatsView? = null

    override fun updateFactAndImage() {
        coroutineScope.launch {
            val result = fetchCatFactWithImage()
            _catsView?.populate(result)
            if (result is Result.Error && result.throwable !is SocketTimeoutException) {
                CrashMonitor.trackWarning(result.throwable)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        coroutineScope.cancel()
        _catsView = null
    }

    private suspend fun getRandomCatFact(): Fact = withContext(Dispatchers.IO) {
        catsService.getCatFact()
    }

    private suspend fun getCatImageInfo(): CatImage = withContext(Dispatchers.IO) {
        catsService.getCatImageInfo().first()
    }

    private suspend fun fetchCatFactWithImage(): Result<UiState> {
        return supervisorScope {
            val catFact = async {
                getRandomCatFact()
            }
            val catImage = async {
                getCatImageInfo()
            }
            try {
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
    }

    private class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext =
            Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}

interface ICatsPresenter {
    fun updateFactAndImage()
}