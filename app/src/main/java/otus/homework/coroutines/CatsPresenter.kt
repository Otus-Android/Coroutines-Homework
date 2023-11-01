package otus.homework.coroutines

import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsFactsService,
    private val imagesService: CatsImagesService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main +
                CoroutineName("CatsCoroutine")
    )
    private val imageLoader by lazy { Picasso.get() }

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val cat = imagesService.getCatImage().first()
                val imageBitmap = withContext(Dispatchers.IO) {
                    imageLoader.load(cat.imageUrl).get()
                }
                _catsView?.populate(
                    Cat(
                        image = imageBitmap,
                        text = catsService.getCatFact().text,
                    )
                )
            } catch (_: SocketTimeoutException) {
                _catsView?.showErrorToast()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showErrorToast(e.message)
            }
        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.coroutineContext.cancelChildren()
        _catsView = null
    }

}