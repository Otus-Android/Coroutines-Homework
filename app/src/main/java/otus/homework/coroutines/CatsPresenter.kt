package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImage
) {
    private var job: Job? = null
    private var _catsView: ICatsView? = null
    fun onInitComplete() {
        job = PresenterScope().launch {
            try {
                val catFact = async {catsService.getCatFact()}
                    .await()
                val catImage = async {catsImageService.getCatImage()}
                    .await()

                val catResponse = CatResponse(catFact = catFact, catImage = catImage.elementAt(0).url )
                _catsView?.populate(catResponse)
            }
            catch (exception: SocketTimeoutException){
                _catsView?.toast("Не удалось получить ответ от сервера")
            }

            catch (t: Throwable){
                t.message?.let {
                    _catsView?.toast(it)
                    CrashMonitor.trackWarning(it)
                }

            }
        }
    }

   fun attachView(catsView: ICatsView) {
       _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
    }
}