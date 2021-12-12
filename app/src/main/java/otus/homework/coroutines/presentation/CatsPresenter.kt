package otus.homework.coroutines.presentation

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.PresenterScope
import otus.homework.coroutines.data.CatResult
import otus.homework.coroutines.data.CatsFactService
import otus.homework.coroutines.data.CatsImageService
import otus.homework.coroutines.data.dto.FactDto
import otus.homework.coroutines.data.dto.ImageDto
import otus.homework.coroutines.presentation.model.FactModel
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsImageService: CatsImageService,
    private val catsFactService: CatsFactService
) {
    private var presenterScope = PresenterScope()
    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { s, e ->
        CrashMonitor.trackWarning()
    }

    companion object {
        const val EMPTY_VALUE = ""
    }

    fun onInitComplete() {
        presenterScope.launch(exceptionHandler) {
            supervisorScope {

                val fact = async { getFact() }
                val image = async { getImage() }

                try {
                    val factResult = fact.await().text
                    val imageResult = image.await().file
                    val model = FactModel(
                        image = imageResult,
                        fact = factResult
                    )

                    val result = CatResult.Success(model)
                    successResult(result)

                } catch (e: SocketTimeoutException) {
                    _catsView?.showToast("${e.message}")
                    EMPTY_VALUE
                }
            }
        }
    }

    private fun successResult(model: CatResult<FactModel>) {
        _catsView?.populate(model)
    }

    private suspend fun getFact(): FactDto {
        delay(1000)
        return catsFactService.getFact()
    }

    private suspend fun getImage(): ImageDto = catsImageService.getCatImage()

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.coroutineContext.cancel()
    }
}