package otus.homework.coroutines

import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException

class CatsPresenter(
    private val mainActivity: MainActivity,
    private val catsService: CatsService
) {
    private var job: Job = Job()
    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        runBlocking {
            job = launch(CoroutineName("CatsCoroutine")) {
                try {
                    val result = catsService.getCatFact()
                    if (result.isSuccessful && result.body() != null) {
                        _catsView?.populate(result.body()!!)
                    } else {
                        CrashMonitor.trackWarning()
                    }
                } catch (socketTimeoutException : SocketTimeoutException) {
                    Toast.makeText(mainActivity, "Не удалось получить ответ от сервером",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e:Exception){
                    Log.d("otus.homework.coroutines.CrashMonitor", e.toString())
                    Toast.makeText(mainActivity, "exception.message", Toast.LENGTH_SHORT).show()
                }
            }
            job.join()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}