package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val context: Context,
    private val catsService: CatsService,
    private val catsImage: CatsImage,
) {

    private var _catsView: ICatsView? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is SocketTimeoutException) {
            Toast.makeText(context,  "Не удалось получить ответ от сервера", Toast.LENGTH_LONG)
        } else {
            CrashMonitor.trackWarning()
            Toast.makeText(context, throwable.message, Toast.LENGTH_LONG)
        }
    }

     fun onInitComplete() {
         Log.d("Cats", "dcdc")
            CoroutineScope(Dispatchers.Main + coroutineExceptionHandler).launch {
            runCatching {
                Log.d("Cats", "Start")
                val responseInfo = catsService.getCatFact()
                val responseImage = catsImage.getCatImage()
                val resultInfo = responseInfo.body()
                val resultImage = responseImage.body()
                Log.d("Cats", resultImage.toString())
                if (responseInfo.isSuccessful && resultInfo != null && responseImage.isSuccessful && resultImage != null) {
                    _catsView?.populate(Result.Success(responseInfo.body()!!, responseImage.body()!![0].url))
                }
            }.onFailure {
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}
