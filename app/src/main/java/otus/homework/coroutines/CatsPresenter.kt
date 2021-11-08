package otus.homework.coroutines

import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class PresenterScope : CoroutineScope {
  override val coroutineContext: CoroutineContext =
    Dispatchers.Main + CoroutineName("CatsCoroutine")
}

class CatsPresenter(
  private val catsService: CatsService
) {

  private var _catsView: ICatsView? = null
  private var job: Job? = null
  private val scope = PresenterScope()

  fun onInitComplete() {
    job = scope.launch {
      try {
        val fact = catsService.getCatFact()
        _catsView?.populate(fact)
      } catch (e: Exception) {
        handle(e)
      }
    }
  }

  private fun handle(exception: Exception) = when (exception) {
    is SocketTimeoutException -> {
      _catsView?.show("No response from server")
    }
    else -> {
      _catsView?.show(exception.message ?: "something nasty happened")
      CrashMonitor.trackWarning(exception)
    }
  }

  fun attachView(catsView: ICatsView) {
    _catsView = catsView
  }

  fun detachView() {
    _catsView = null
  }

  fun onStop() {
    job?.cancel()
  }
}

