package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            //Обрабатываем непредвиденную ошибку
            CrashMonitor.trackWarning(throwable)
            _catsView?.showToast(throwable.message ?: throwable.toString())
        }
        ) {
            try {
                val response = catsService.getCatFact()

                if (response.isSuccessful && response.body() != null) {
                    Log.e("CatsPresenter", "Success!!!")
                    _catsView?.populate(response.body()!!)
                }
                else {
                    throw Exception(if (response.body() == null) "Incorrect data from server" else response.message())
                }
            }
            //Обрабатываем определённые ошибки
            catch (e: SocketTimeoutException) {
                Log.e("CatsPresenter", "Error: $e")
                _catsView?.showToast("Не удалось получить ответ от сервера")

            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelRequests() {
        presenterScope.cancel()
    }

}

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main + CoroutineName("CatsCoroutine")

}