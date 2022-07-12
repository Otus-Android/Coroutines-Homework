package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
  private val catsService: CatsService
) : CoroutineScope {

  override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.Main + CoroutineName("CatsCoroutine")

  private var _catsView: ICatsView? = null

  fun onInitComplete() {
    launch {
      try {
        val catFact = catsService.getCatFact()
        _catsView?.populate(catFact)
      } catch (e: Exception) {
        when (e) {
          is SocketTimeoutException -> {
            _catsView?.showToast("Не удалось получить ответ от сервера")
          }
          else -> {
            _catsView?.showToast("${e.message}")
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
    this.cancel()
  }
}