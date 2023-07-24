package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val context: Context,
    mainDispatcher: CoroutineDispatcher,
) {
    private var _catsView: ICatsView? = null
    private var presenterScope = CoroutineScope(mainDispatcher + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {

        presenterScope.launch() {
            try {
                val factStr: Deferred<String> = async { updateFact() }
                val imageUrl: Deferred<String> = async { updateImage() }
                fakePopulate(factStr.await(), imageUrl.await())
                //_catsView?.populate(factStr, imageUrl)
            } catch (cancel: CancellationException) {
                throw cancel
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

    private fun fakePopulate(factStr: String, imageUrl: String){}

    private suspend fun updateFact(): String {
        Log.i("HW1_TEST_TAG", "updateFact start")
        val response: Response<Fact> = catsService.getCatFact()
        try {
            if (response.isSuccessful && response.body() != null) {
                return response.body()!!.text
            }
            return ""
        } finally {
            Log.i("HW1_TEST_TAG", "updateFact end")
        }
    }

    private suspend fun updateImage(): String {
        Log.i("HW1_TEST_TAG", "updateImage start")
        val response: Response<ArrayList<Image>> = imageService.getCatImage()
        try {
            if (response.isSuccessful && response.body() != null) {
                return response.body()!![0].url
            }
            return ""
        } finally {
            Log.i("HW1_TEST_TAG", "updateImage end")
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