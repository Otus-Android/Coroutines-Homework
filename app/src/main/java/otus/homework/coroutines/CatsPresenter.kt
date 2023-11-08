package otus.homework.coroutines

import android.content.Context
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService,
    private val context: Context,
    private val catMapper: CatsMapper
) {

    companion object {

        private const val CATS_COROUTINES = "CatsCoroutine"
        private const val SOCKET_TIMEOUT_EXCEPTION = "Не удалось получить ответ от сервера"
        private const val ERROR_MESSAGE = "Произошла ошибка"
        private const val EMPTY_URL = ""
    }

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName(CATS_COROUTINES))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factDeferred = presenterScope.async { catsService.getCatFact() }
                val imageDeferred = presenterScope.async { imageCatsService.getCatImage() }
                val response = factDeferred.await()
                val imageResponse = imageDeferred.await()

                if ((response.isSuccessful && response.body() != null) && imageResponse.first().url != EMPTY_URL) {
                    val factAndImage = catMapper.toFactAndImage(
                        fact = response.body()?.fact,
                        image = imageResponse.first().url
                    )
                    _catsView?.populate(factAndImage)
                }
            } catch (e: java.net.SocketTimeoutException) {
                CrashMonitor.trackWarning(context, SOCKET_TIMEOUT_EXCEPTION)
            } catch (e: Exception) {
                CrashMonitor.trackWarning(context, e.message ?: ERROR_MESSAGE)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}