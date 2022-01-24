package otus.homework.coroutines.presentation.presentor

import kotlinx.coroutines.*
import otus.homework.coroutines.IResourceProvider
import otus.homework.coroutines.R
import otus.homework.coroutines.data.Cat
import otus.homework.coroutines.presentation.view.ICatsView
import otus.homework.coroutines.service.CatFactService
import otus.homework.coroutines.service.CatPictureService
import otus.homework.coroutines.service.CrashMonitor
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactService: CatFactService,
    private val catPictureService: CatPictureService,
    private val resources: IResourceProvider
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    private lateinit var job: Job

    fun onInitComplete() {
        job = presenterScope.launch {
            supervisorScope {
                val fact = async {
                    catFactService.getCatFact()
                }
                val picture = async {
                    catPictureService.getCatPicture()
                }

                try {
                    _catsView?.populate(
                        cat = Cat(
                            text = fact.await().text,
                            picture = picture.await().file
                        )
                    )
                } catch (error: SocketTimeoutException) {
                    _catsView?.showToast(resources.getString(R.string.server_exception_message))
                } catch (error: Throwable) {
                    _catsView?.showToast(error.message)
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        if (!job.isCancelled) job.cancel()
        _catsView = null
    }
}