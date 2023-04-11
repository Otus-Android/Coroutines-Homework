package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val factService: FactService,
    private val picService: PicService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope: CoroutineScope = PresenterScope
    private lateinit var job: Job
    fun onInitComplete() {
      job = presenterScope.launch {
          try {
              val fact = async { factService.getCatFact().fact }
              val picUrl = async { picService.getCatPic()[0].url }
              _catsView?.populate(
                  CatData(
                      picUrl.await(),
                      fact.await()
                  )
              )
          } catch (e: SocketTimeoutException) {
              _catsView?.showToast("Не удалось получить ответ от сервера")
          } catch (e: Exception) {
              _catsView?.showToast(e.message.toString())
              CrashMonitor.trackWarning()
          }
      }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}