package otus.homework.coroutines

import java.lang.Exception
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsService
) {

  private var _catsView: ICatsView? = null

  private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

  private var job: Job? = null

  fun onInitComplete() {
    job =
        presenterScope.launch {
          try {
            val catInfo = fetchCatInfo()
            _catsView?.populate(catInfo)
          } catch (e: Exception) {
            handleError(e)
          }
        }
  }

  private suspend fun fetchCatInfo(): CatInfo {
    val factDeferred = presenterScope.async { catsService.getCatFact().body()?.fact.orEmpty() }
    val imageDeferred = presenterScope.async { catsImageService.getCatImage().body().orEmpty() }

    return CatInfo(factDeferred.await(), imageDeferred.await())
  }

  fun attachView(catsView: ICatsView) {
    _catsView = catsView
  }

  fun detachView() {
    _catsView = null
    job?.cancel()
    job = null
  }

  private fun handleError(e: Exception) {
    when (e) {
      is SocketTimeoutException -> {
        _catsView?.showToast("Не удалось получить ответ от сервера")
      }
      is CancellationException -> {
        throw CancellationException()
      }
      else -> {
        CrashMonitor.trackWarning()
        e.message?.let { message -> _catsView?.showToast(message) }
      }
    }
  }
}
