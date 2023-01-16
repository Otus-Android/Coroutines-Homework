package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
) {

    private val scope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch {
            val factAsync = async { loadFact() }
            val catImageAsync = async { loadImageUrl() }

            val factText = factAsync.await()?.fact
            val imageUrl = catImageAsync.await()?.url

            _catsView?.populate(catInfo = CatInfo(text = factText, imageUrl = imageUrl))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun closeScope() {
        scope.cancel()
    }

    private suspend fun loadFact(): FactNew? = invokeSafely { catsService.getCatFact() } as FactNew?


    private suspend fun loadImageUrl(): CatImage? =
        invokeSafely { catsImageService.getCatImage() } as CatImage?

    private suspend fun invokeSafely(block: suspend () -> Response<out Any>): Any? {
        try {
            val response = block()
            if (response.isSuccessful && response.body() != null) {
                return response.body()
            } else {
                CrashMonitor.trackWarning()
            }
        } catch (e: SocketTimeoutException) {
            _catsView?.showConnectionErrorMessage()
        } catch (e: Exception) {
            CrashMonitor.trackWarning()
            _catsView?.showMessage(e.message)
        }
        return null
    }
}