package otus.homework.coroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.support.v4.app.INotificationSideChannel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.tools.CrashMonitor
import otus.homework.coroutines.services.DiContainer
import otus.homework.coroutines.tools.PresenterScope
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.URL

interface ICatsPresenter {
    fun onInitComplete()
    fun attachView(catsView: ICatsView)
    fun detachView()
    fun stop()
    fun doReadThings()
}

class CatsViewModel() : ViewModel(), ICatsPresenter {

    private val catsData = CatsData()
    private var catView: ICatsView? = null
        set(value) {
            field = value
            viewModelScope.launch {
                value?.let {
                    catsData.resultFact?.let {
                        value.populateFact(it)
                    }
                    catsData.resultImage?.let {
                        value.populateImage(it)
                    }
                }
            }
        }
    private val diContainer = DiContainer()

    private val catsFactService = diContainer.factsService
    private val imagessService = diContainer.imagessService

    data class CatsData(var resultFact: String? = "", var resultImage: Bitmap? = null)

    init {
        doReadThings()
    }

    override fun doReadThings() {
        val jobFacts = viewModelScope.launch {
            try {
                val catFact = catsFactService.getCatFact()
                catsData.resultFact = catFact.text
            } catch (e: SocketTimeoutException) {
                showToast("Не удалось получить ответ от сервера")
            } catch (exc: Exception) {
                CrashMonitor.trackWarning(exc.message.toString())
                val s = if (exc.message != null) exc.message!! else ""
                showToast(s)
            }
        }
        val jobImages = viewModelScope.launch {
            try {
                val catImageSource = imagessService.getCatImageSource()
                catImageSource?.let {
                    withContext(Dispatchers.IO) {
                        val inputStream = URL(catImageSource.file).openStream()
                        val image = BitmapFactory.decodeStream(inputStream)
                        catsData.resultImage = image
                    }
                }
            } catch (e: SocketTimeoutException) {
                showToast("Не удалось получить ответ от сервера")
            } catch (exc: Exception) {
                CrashMonitor.trackWarning(exc.message.toString())
                val s = if (exc.message != null) exc.message!! else ""
                showToast(s)
            }
        }
        viewModelScope.launch {
            jobImages.join()
            jobFacts.join()
            catsData.resultFact?.let {
                catView?.populateFact(it)
            }
            catsData.resultImage?.let {
                catView?.populateImage(it)
            }
        }
    }

    private fun showToast(s: String) {
        viewModelScope.launch {
            catView?.showToast(s)
        }
    }

    override fun onInitComplete() {
    }

    override fun attachView(catsView: ICatsView) {
        catView = catsView
    }

    override fun detachView() {
        catView = null
    }

    override fun stop() {
    }

}