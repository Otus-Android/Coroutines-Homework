package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private var currentJob: Job? = null

    private val presenterScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {

        currentJob?.cancel()

        currentJob = presenterScope.launch {
            try {

                _catsView?.showLoading(true)


                val deferredFact = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }

                val deferredImage = async(Dispatchers.IO) {
                    catsImageService.getCatImage()
                }


                val fact = deferredFact.await()
                val image = deferredImage.await()

                val enhancedFact = Fact(
                    fact = fact.fact,
                    length = fact.length,
                    imageUrl = image.file // Добавляем URL картинки
                )

                // Передаем в существующий метод populate
                _catsView?.populate(enhancedFact)

                _catsView?.showLoading(false)

            } catch (e: CancellationException) {

                _catsView?.showLoading(false)
            } catch (e: SocketTimeoutException) {
                _catsView?.showLoading(false)
                handleError(e, "Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _catsView?.showLoading(false)
                handleError(e, e.message ?: "Неизвестная ошибка")
            }
        }
    }

    private fun handleError(e: Exception, message: String) {
        CrashMonitor.trackWarning()

        _catsView?.showToast(message)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop() {
        presenterScope.coroutineContext.cancelChildren()
    }

    fun cancel() {
        presenterScope.cancel()
    }
}