package otus.homework.coroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
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

    private val diContainer = DiContainer()

    private val catsFactService = diContainer.factsService
    private val imagessService = diContainer.imagessService

    private var jobFacts: Job? = null
    private var jobImages: Job? = null

    //    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    private val catsData = CatsData()

    class CatsData {
        private val presenterScope = PresenterScope()
        var catView: ICatsView? = null
            set(value) {
                field = value
                populateFact()
                populateImage()
            }

        //        var fact: String? = null
//            set(value) {
//                field = value
//                populateFact()
//            }
        var resultFact: Result.SuccessFact? = null
            set(value) {
                field = value
                populateFact()
            }

        //        var image: Bitmap? = null
//            set(value) {
//                field = value
//                populateImage()
//            }
        var resultImage: Result.SuccessImage? = null
            set(value) {
                field = value
                populateImage()
            }

        fun populateFact() {
            presenterScope.launch {
//                fact?.let { catView?.populateFact(fact!!) }
                resultFact?.let { catView?.populateResult(resultFact!!) }
            }
        }

        fun populateImage() {
            presenterScope.launch {
//                image?.let { catView?.populateImage(image!!) }
                resultImage?.let { catView?.populateResult(resultImage!!) }
            }
        }

        fun showToast(text: String) {
            catView?.showToast(text)
        }

    }

    init {
        doReadThings()
    }


    override fun doReadThings() {
        jobFacts = presenterScope.launch {
            try {
                val catFact = catsFactService.getCatFact()
//                catsData.fact = catFact.text
                catsData.resultFact = Result.SuccessFact(catFact)
            } catch (e: SocketTimeoutException) {
                catsData.showToast("Не удалось получить ответ от сервера")
            } catch (exc: Exception) {
                CrashMonitor.trackWarning(exc.message.toString())
                val s = if (exc.message != null) exc.message!! else ""
                catsData.showToast(s)
            }
        }
        jobImages = presenterScope.launch {
            try {
                val catImageSource = imagessService.getCatImageSource()
                catImageSource?.let {
                    withContext(Dispatchers.IO) {
                        val inputStream = URL(catImageSource.file).openStream()
                        val image = BitmapFactory.decodeStream(inputStream)
//                        catsData.image = image
                        catsData.resultImage = Result.SuccessImage(image)
                    }
                }
            } catch (e: SocketTimeoutException) {
                catsData.showToast("Не удалось получить ответ от сервера")
            } catch (exc: Exception) {
                CrashMonitor.trackWarning(exc.message.toString())
                val s = if (exc.message != null) exc.message!! else ""
                catsData.showToast(s)
            }
        }
    }

    override fun onInitComplete() {
    }

    override fun attachView(catsView: ICatsView) {
        catsData.catView = catsView
    }

    override fun detachView() {
        catsData.catView = null
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

}