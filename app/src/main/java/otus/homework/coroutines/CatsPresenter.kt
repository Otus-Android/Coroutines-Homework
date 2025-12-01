package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor.trackWarning
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
) {

    private var _catsView: ICatsView? = null
    private val scopeCats = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))


    fun onInitComplete() {

        scopeCats.launch {
            try {
                val responseFact = async {catsService.getCatFact()}
                val responseImage = async {imageService.getImage().firstOrNull()}

                _catsView?.populate(CatDataModel(responseFact.await(), responseImage.await()?.url))


            } catch (e: SocketTimeoutException) {
                val error = e.message ?: "Не удалось получить ответ от сервера"
                _catsView?.showToast(error)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error"
                trackWarning(errorMessage)
                _catsView?.showToast(errorMessage)
            }
        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop() {
        scopeCats.cancel()
    }
}
