package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val catPictureService: CatPictureService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            //Обрабатываем непредвиденную ошибку
            CrashMonitor.trackWarning(throwable)
        }
        ) {
            try {
                //Запускаем запросы одновременно
                val deferredFact = async { catsService.getCatFact() }
                val deferredPicture = async { catPictureService.getCatPicture() }

                //Ждём выполнение всех запросов
                val fact = deferredFact.await()
                val picture = deferredPicture.await()

                Log.e("CatsPresenter", "requests success!!!")
                _catsView?.populate(CatViewModel(fact.text, picture.url))
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