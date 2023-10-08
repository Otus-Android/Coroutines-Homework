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
    }

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName(CATS_COROUTINES))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val response = catsService.getCatFact()
                val imageResponse = imageCatsService.getCatImage()
                if ((response.isSuccessful && response.body() != null) && (imageResponse.isSuccessful &&
                            imageResponse.body() != null)) {
                    val factAndImage = catMapper.toFactAndImage(
                        fact = response.body()?.fact,
                        image = imageResponse.body()?.image
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