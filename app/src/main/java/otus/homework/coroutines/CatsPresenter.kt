package otus.homework.coroutines

import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class PresenterScope : CoroutineScope {
  override val coroutineContext: CoroutineContext =
    Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
}

class CatsPresenter(
  private val service: CatsService
) {

  private var _catsView: ICatsView? = null
  private var job: Job? = null
  private val scope = PresenterScope()

  fun onInitComplete() {
    job = scope.launch {
      val catFact = async(Dispatchers.IO) { service.getCatFact() }
      val catImage = async(Dispatchers.IO) { service.getCatImage() }
      try {
        _catsView?.populate((Cat(catFact.await().text, catImage.await().src)))
      } catch (e: Exception) {
        handle(e)
      }
    }
  }

  private fun handle(exception: Exception) = when (exception) {
    is SocketTimeoutException -> _catsView?.show("No response from server")
    is CancellationException -> throw exception
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

