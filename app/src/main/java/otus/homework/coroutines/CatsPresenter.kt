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
            _catsView?.showToast(throwable.message ?: throwable.toString())
        }
        ) {
            try {
                //Запускаем запросы одновременно
                val deferredFact = async { catsService.getCatFact() }
                val deferredPicture = async { catPictureService.getCatPicture() }

                //Ждём выполнение всех запросов
                val responseFact = deferredFact.await()
                val responsePicture = deferredPicture.await()

                if (
                    responseFact.isSuccessful
                    && responseFact.body() != null

                    && responsePicture.isSuccessful
                    && responsePicture.body() != null
                    && (responsePicture.body() as Picture).url.isNotEmpty()
                ) {
                    Log.e("CatsPresenter", "requests success!!!")
                    val fact = responseFact.body() as Fact
                    val picture = responsePicture.body() as Picture
                    _catsView?.populate(CatViewModel(fact.text, picture.url))
                } else {
                    throw Exception(
                        if (responseFact.body() == null || responsePicture.body() == null) {
                            "Incorrect data from server"
                        }
                        else {
                            if (!responseFact.isSuccessful && responsePicture.isSuccessful) {
                                responseFact.message()
                            }
                            else if (responseFact.isSuccessful && !responsePicture.isSuccessful) {
                                responsePicture.message()
                            }
                            else {
                                responseFact.message() + " " + responsePicture.message()
                            }
                        })
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