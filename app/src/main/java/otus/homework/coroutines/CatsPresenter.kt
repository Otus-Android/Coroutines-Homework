package otus.homework.coroutines

import java.lang.Exception
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CatsPresenter(private val catsService: CatsService) {

  private var _catsView: ICatsView? = null

  private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

  private var job: Job? = null

  fun onInitComplete() {
    job =
        presenterScope.launch {
          try {
            val result = catsService.getCatFact()
            result.body()?.let { fact -> _catsView?.populate(fact) }
          } catch (e: Exception) {
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
  }

  fun attachView(catsView: ICatsView) {
    _catsView = catsView
  }

  fun detachView() {
    _catsView = null
    job?.cancel()
    job = null
  }
}
