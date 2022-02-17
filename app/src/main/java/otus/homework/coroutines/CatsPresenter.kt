package otus.homework.coroutines

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import otus.homework.coroutines.services.CatsFactService
import otus.homework.coroutines.services.CatsImageService
import otus.homework.coroutines.tools.CrashMonitor
import otus.homework.coroutines.tools.PresenterScope
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.URL

class CatsPresenter(
    private val _activityContext: MainActivity,
    private val catsFactService: CatsFactService,
    private val imagessService: CatsImageService
) : ICatsPresenter {

    private var jobFacts: Job? = null
    private var jobImages: Job? = null
    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    override fun onInitComplete() {
        doReadThings()
    }

    suspend fun showToast(text: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(_activityContext, text, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun detachView() {
        _catsView = null
    }

    override fun stop() {
        jobFacts?.let {
            if (it.isActive)
                it.cancel()
        }
        jobImages?.let {
            if (it.isActive)
                it.cancel()
        }
    }

    override fun doReadThings() {
        runBlocking() {
            jobFacts = presenterScope.launch(CoroutineName("CatsCoroutine")) {
                try {
                    val catFact = catsFactService.getCatFact()
                    catFact?.let { _catsView?.populateFact(catFact.text) }
                    Log.w("#RESP", catFact.toString())
                } catch (e: SocketTimeoutException) {
                    showToast("Не удалось получить ответ от сервером")
                } catch (exc: Exception) {
                    CrashMonitor.trackWarning(exc.message.toString())
                    val s = if (exc.message != null) exc.message!! else ""
                    showToast(s)
                }
            }
            jobImages = presenterScope.launch(Dispatchers.IO) {
                try {
                    val catImageSource = imagessService.getCatImageSource()
                    catImageSource?.let {
                        val inputStream = URL(catImageSource.file).openStream()
                        val image = BitmapFactory.decodeStream(inputStream)
                        _catsView?.populateImage(image)
                    }
                    Log.w("#RESP", catImageSource.toString())
                } catch (e: SocketTimeoutException) {
                    showToast("Не удалось получить ответ от сервером")
                } catch (exc: Exception) {
                    CrashMonitor.trackWarning(exc.message.toString())
                    val s = if (exc.message != null) exc.message!! else ""
                    showToast(s)
                }
            }
        }
    }
}