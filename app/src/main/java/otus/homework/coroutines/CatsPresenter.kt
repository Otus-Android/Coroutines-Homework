package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val pictureService: PictureService
) {

    private var _catsView: ICatsView? = null
    private var jobCat: Job? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        jobCat = presenterScope.launch {
            try {
                val catFact = async(Dispatchers.IO) { catsService.getCatFact() }
                val picture = async(Dispatchers.IO) { pictureService.getCatPicture() }

                _catsView?.populate(catFact.await(), picture.await())

            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.toast("Не удалось получить ответ от сервера")
                    }
                    is CancellationException -> {
                        throw e
                    }
                    else -> {
                        CrashMonitor.trackWarning(e.message!!)
                        _catsView?.toast(e.message!!)
                    }
                }
            }
        }
    }



        fun attachView(catsView: ICatsView) {
            _catsView = catsView
        }

        fun detachView() {
            _catsView = null
            jobCat?.cancel()
        }


}