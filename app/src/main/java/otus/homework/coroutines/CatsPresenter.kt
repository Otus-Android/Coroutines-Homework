package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val context: Context,
    mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) {
    private var _catsView: ICatsView? = null
    private var PresenterScope = CoroutineScope(mainDispatcher + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {

        PresenterScope.launch() {
            try {
                //_catsView?.populate(updateFact(), updataImage())
            } catch (error: java.net.SocketTimeoutException) { // add java.net.UnknownHostException ???
                Toast.makeText(context, "Не удалось получить ответ от сервером", Toast.LENGTH_LONG)
                    .show()
            } catch (error: Exception) {
                CrashMonitor.trackWarning(error.message)
                Toast.makeText(context, "Ошибка: ${error.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    suspend fun updateFact(): String {
        val response: Response<Fact> = withContext(ioDispatcher) {
            return@withContext catsService.getCatFact()
        }
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.text
        }
        return ""
    }

    suspend fun updataImage(): String {
        val response: Response<ArrayList<Image>> = withContext(ioDispatcher) {
            return@withContext imageService.getCatImage()
        }
        if (response.isSuccessful && response.body() != null) {
            return response.body()!![0].url
        }
        return ""
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        PresenterScope.cancel()
    }
}