package otus.homework.coroutines.presenter

import android.util.Log
import kotlinx.coroutines.*
import otus.homework.coroutines.ui.ICatsView
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.utils.CrashMonitor
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val TAG = "CatsPresenter"

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        // used to track warnings in CrashMonitor
        val exceptionHandler = createExceptionHandler()
        coroutineScope.launch(exceptionHandler) {
            try {
                val factResponse = async(ioDispatcher) {
                    catsService.getCatFact()
                }

                val picResponse = async(ioDispatcher) {
                    catsService.getCatPicture()
                }
                _catsView?.populate(CatModel(factResponse.await(), picResponse.await()))

            } catch (t: Throwable) {
                onFailure(t)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelCurrentJob() {
        Log.d(TAG, "cancelCurrentJob: is called")
        coroutineScope.cancel()
    }

    private fun createExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }
    }

    private fun onFailure(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException -> {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            }
            // need to rethrow CancellationException to allow cancelling child coroutines
            is CancellationException -> {
                throw throwable
            }
            else -> {
                _catsView?.showToast(throwable.message ?: "Unknown problem")
            }
        }
    }
}
