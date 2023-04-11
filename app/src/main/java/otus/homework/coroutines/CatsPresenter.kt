package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private val presenterScope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try{
                val fact = catsService.getCatFact()
                val img = imageService.getCatImg()
                _catsView?.populate(Data(fact.text, img[0].url))
            }catch (e: java.net.SocketTimeoutException){
                Util.showToast(R.string.no_connect_server)
            }catch (e:Exception){
                CrashMonitor.trackWarning()
                Util.showToast(e.message.toString())
            }

        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}