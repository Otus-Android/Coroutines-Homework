package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

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
                    val resultInfo = catsService.getCatFact()
                    _catsView?.populate(
                        Result.Success(
                            resultInfo,
                            null,
                        )
                    )
                }
                launch {
                    val resultImage = catsImage.getCatImage()
                    Log.d("Cats", resultImage.toString())
                    _catsView?.populate(
                        Result.Success(
                            null,
                            resultImage[0].url
                        )
                    )
                }
            }.onFailure {
                if (it is SocketTimeoutException) {
                    CrashMonitor.trackWarning()
                    Toast.makeText(
                        context,
                        "Не удалось получить ответ от сервера",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (it is CancellationException) {
                    CrashMonitor.trackWarning()
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
