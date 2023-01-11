package otus.homework.coroutines.ui

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageService
import otus.homework.coroutines.ui.model.FactVO
import otus.homework.coroutines.utils.CrashMonitor
import java.net.SocketTimeoutException

class CatsPresenter(
  private val catsService: CatsService,
  private val imageService: ImageService,
) : PresenterCoroutineScope("CatsCoroutine") {

  companion object {
    private const val TAG = "CatsPresenter"
  }

  private var _catsView: ICatsView? = null
  private var catsParentJob: Job? = null

  private val catsCoroutineExceptionHandler = CoroutineExceptionHandler { _, th ->
    CrashMonitor.trackWarning(TAG, th)
    _catsView?.respondOnError(th.message ?: "")
  }

  fun onInitComplete() {
    catsParentJob = launch(catsCoroutineExceptionHandler) {
      try {
        val factJob = async(io()) { catsService.getCatFact() }
        val fact = factJob.await()

        val imageJob = async(io()) { imageService.getCatImage() }
        val image = imageJob.await()

        val factViewObject = FactVO(fact.fact, image.file)

        _catsView?.populate(factViewObject)

        CrashMonitor.trackDebug(TAG, fact.toString())
        CrashMonitor.trackDebug(TAG, image.toString())
      } catch (ste: SocketTimeoutException) {
        _catsView?.respondOnError("Не удалось получить ответ от сервера")
      }
    }
  }

  fun attachView(catsView: ICatsView) {
    _catsView = catsView
  }

  fun detachView() {
    _catsView = null

    catsParentJob?.cancel()
    catsParentJob = null
  }
}