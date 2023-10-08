package otus.homework.coroutines.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.Cat
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.data.CatFactsService
import otus.homework.coroutines.data.CatImagesService
import otus.homework.coroutines.presentation.ICatsView

class CatsPresenter(
    private val CatFactsService: CatFactsService,
    private val CatImagesService: CatImagesService,
    private val presenterScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factJob = async(Dispatchers.IO) {
                    CatFactsService.getCatFact()
                }
                val imageJob = async(Dispatchers.IO) {
                    CatImagesService.getCatImage()[0]  // TODO wrapper
                }
                val catResult = Cat(factJob.await().fact, imageJob.await().url)
                Log.d("TAG", "Fact is ${catResult.fact}, image is ${catResult.imageUrl}")
                _catsView?.populate(catResult)
            } catch (sockExcept: java.net.SocketTimeoutException) {
                Log.d("TAG", "SockExcept")
                _catsView?.showErrorToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _catsView?.showErrorToast("${e.message}")
                Log.d("TAG", "${e.message}")
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

    fun cancelCoroutine() {
        presenterScope.cancel()   // cancel job + all children
    }
}