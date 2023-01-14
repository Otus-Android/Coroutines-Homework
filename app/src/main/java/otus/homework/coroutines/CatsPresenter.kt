package otus.homework.coroutines

import android.os.Bundle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            val resultFact = fetchData { catsService.getCatFact() }
            val resultImage = fetchData { catsService.getImage("https://aws.random.cat/meow") }
            if (resultFact == null || resultImage == null) return@launch

            val bundle = Bundle()
            bundle.putString("fact", resultFact.fact)
            bundle.putString("image", resultImage.file)
            _catsView?.populate(bundle)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        scope.onStop()
        _catsView = null
    }

    private suspend fun <T> fetchData(block: suspend () -> Response<T>): T? {
        return try {
            val result = block()
            if (!result.isSuccessful && result.body() == null) return null

            result.body()
        } catch (e: SocketTimeoutException) {
            _catsView?.showToast("Не удалось получить ответ от сервером")
            null
        } catch (e: CancellationException) {
            CrashMonitor.trackWarning()
            _catsView?.showToast(e.localizedMessage!!)
            null
        }
    }
}
