package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.dto.CatData
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
  private val catFactsService: CatFactsService,
  private val catImagesService: CatImagesService
) {

  private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    when (throwable) {
      is SocketTimeoutException -> {
        _catsView?.showToast("Не удалось получить ответ от сервера")
      }
      else -> {
        _catsView?.showToast("${throwable.message}")
      }
    }
  }

  private var presenterScope: PresenterScope =
    PresenterScope(coroutineExceptionHandler)

  private var _catsView: ICatsView? = null

  fun onInitComplete() {
    presenterScope.launch {
      val catFact = async {
        catFactsService.getCatFact()
      }
      val catImageUri = async { catImagesService.getCatImageUri().catImageUri }

      _catsView?.populate(CatData(catFact.await().text, catImageUri.await()))
    }
  }

  fun attachView(catsView: ICatsView) {
    _catsView = catsView
  }

  fun detachView() {
    _catsView = null
    presenterScope.cancel()
  }
}

class PresenterScope(private val coroutineExceptionHandler: CoroutineExceptionHandler) : CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.Main + CoroutineName("CatsCoroutine") + coroutineExceptionHandler
}