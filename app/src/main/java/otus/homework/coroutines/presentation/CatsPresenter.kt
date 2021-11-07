package otus.homework.coroutines.presentation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
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
    private var scope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch() {
            try {
                val image = async { getImage() }
                val fact = async { getFact() }
                val model = FactModel(image.await().file, fact.await().text)

                val result = CatResult.Success(model)
                successResult(result)

            } catch (e: Exception) {
                notifyAboutError(e)
            }
        }
    }

    private fun successResult(model: CatResult<FactModel>) {

        _catsView?.populate(model)
    }

    private fun notifyAboutError(exception: Exception) {
        when (exception) {
            is SocketTimeoutException -> _catsView?.showToast("Не удалось получить ответ от сервера")
            is CancellationException -> throw exception
            else -> CrashMonitor.trackWarning()
        }
    }

    private suspend fun getFact(): FactDto {
        throw IllegalArgumentException("Какая-то страшная ошибка")
        return catsFactService.getFact()
    }

    private suspend fun getImage(): ImageDto = catsImageService.getCatImage()

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.coroutineContext.job.cancel()
    }
}