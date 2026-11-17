package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class MainViewModel(private val catsService: CatsService) : ViewModel(), ICatsPresenter {

    private var _catsView: ICatsView? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    val scope = viewModelScope + coroutineExceptionHandler

    override fun updateFactAndImage() {
        scope.launch {
            val result = fetchCatFactWithImage()
            _catsView?.populate(result)
            if (result is Result.Error && result.throwable !is SocketTimeoutException) {
                throw result.throwable
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
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


    @Suppress("UNCHECKED_CAST")
    class Factory(private val catsService: CatsService) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(catsService) as T
        }
    }
}