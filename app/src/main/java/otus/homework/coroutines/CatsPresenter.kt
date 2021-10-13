package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import otus.homework.coroutines.models.PresentModel
import otus.homework.coroutines.services.ImageService
import otus.homework.coroutines.utils.PresenterScope
import java.net.SocketTimeoutException

const val TAG = "Presenter"

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService

){

    val job = PresenterScope()

    private var _catsView: ICatsView? = null

    fun onInitComplete()  {
        job.launch{
            try {

                val responseFact = catsService.getCatFact()
                val responseImage = imageService.getCatImage()

                if (responseFact.isSuccessful && responseImage.isSuccessful) {
                    val presentModel = PresentModel(responseImage.body()!!, responseFact.body()!!)
                    _catsView?.populate(presentModel)
                } else {
                    _catsView?.showToast("Ошибка responseFact: ${responseFact.message()}" +
                            "Ошибка responseFact: ${responseImage.message()}")
                }
            }
            catch (cause: SocketTimeoutException){
                _catsView?.showToast("Не удалось получить ответ от сервера")
                Log.d(TAG, "SocketTimeoutException")
            } catch (cause: Exception){
                _catsView?.showToast("Exception: ${cause.message}")
                Log.d(TAG, "Exception: ${cause.message}")
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