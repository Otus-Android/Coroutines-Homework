package otus.homework.coroutines.ui

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatsUiState
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Image
import otus.homework.coroutines.network.CatsService
import otus.homework.coroutines.network.ImageService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(
        Job() + Dispatchers.Main + CoroutineName(COROUTINE_NAME)
    )

    fun onInitComplete() {
        presenterScope.launch {
            _catsView?.loadingData(true)
            supervisorScope {
                val factDeferred = async { loadFact() }
                val imageDeferred = async { loadCatImage() }

                val fact = runCustomCatch { factDeferred.await()?.fact }
                val image = runCustomCatch { imageDeferred.await()?.file }

                _catsView?.populate(
                    CatsUiState(
                        fact = fact ?: "",
                        imageUrl = image
                    )
                )
                _catsView?.loadingData(false)
            }
        }
    }

    private suspend fun loadFact(): Fact? {
        val response = catsService.getCatFact()
        return if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }

    private suspend fun loadCatImage(): Image? {
        val response = imageService.getCatImage()
        return if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }

    private suspend fun <T> runCustomCatch(block: suspend () -> T?): T? {
        return try {
            block()
        } catch (e: SocketTimeoutException) {
            showMessage(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
            null
        } catch (e: Exception) {
            CrashMonitor.trackWarning(e)
            showMessage(e.message)
            null
        }
    }

    private fun showMessage(message: String?) {
        _catsView?.showToast(message ?: UNKNOWN_ERROR)
    }

    private companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
        private const val UNKNOWN_ERROR = "Неизвестная ошибка"
        private const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"
    }
}