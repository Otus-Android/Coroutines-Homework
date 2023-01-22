package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val context: Context,
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {
    private val presenterScope = CoroutineScope(
        Job() + Dispatchers.Main + CoroutineName("CatsCoroutine")
    )
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            supervisorScope {
                val factDeffered = async {
                    catsService.getCatFact()
                }
                val imageDeffered = async {
                    catsImageService.getCatImage()
                }
                try {
                    val factResponse = factDeffered.await()
                    val imageResponse = imageDeffered.await()
                    if (
                        factResponse.isSuccessful && factResponse.body() != null
                        && imageResponse.isSuccessful && imageResponse.body() != null
                    ) {
                        _catsView?.populate(CatViewData(
                            factResponse.body()!!.fact,
                            imageResponse.body()!!.file
                        ))
                    }
                } catch (e: SocketTimeoutException) {
                    Toast.makeText(context, R.string.service_response_error, Toast.LENGTH_SHORT).show()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}