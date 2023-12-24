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
        CrashMonitor.trackWarning()
        Toast.makeText(context, throwable.message, Toast.LENGTH_LONG)
    }

    fun onInitComplete() {
        Log.d("Cats", "dcdc")
        CoroutineScope(Dispatchers.Main + coroutineExceptionHandler).launch {
            runCatching {
                launch {
                    val responseInfo = catsService.getCatFact()
                    val resultInfo = responseInfo.body()
                    if (responseInfo.isSuccessful && resultInfo != null) {
                        _catsView?.populate(
                            Result.Success(
                                responseInfo.body()!!,
                                null,
                            )
                        )
                    }
                }
                launch {
                    val responseImage = catsImage.getCatImage()
                    val resultImage = responseImage.body()
                    Log.d("Cats", resultImage.toString())
                    if ( responseImage.isSuccessful && resultImage != null) {
                        _catsView?.populate(
                            Result.Success(
                                null,
                                responseImage.body()!![0].url
                            )
                        )
                    }
                }
            }.onFailure {
                if (it is SocketTimeoutException) {
                    CrashMonitor.trackWarning()
                    Toast.makeText(
                        context,
                        "Не удалось получить ответ от сервера",
                        Toast.LENGTH_LONG
                    ).show()
                }
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
