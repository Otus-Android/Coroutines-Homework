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
    private var scope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch() {
            try {
                val image = async { getImage() }
                val fact = async { getFact() }
                val results = awaitAll(image, fact)

                val model = buildResult(results)
                withContext(Dispatchers.Main) { successResult(model) }

            } catch (e: Exception) {
                notifyAboutError(e)
            }
        }
    }

    private fun buildResult(data: List<Any>): CatResult<FactModel> {
        var image = ""
        var fact = ""
        data.forEach {
            if (it is ImageDto) {
                image = it.file
            }
            if (it is FactDto) {
                fact = it.text
            }
        }
        return CatResult.Success(FactModel(image, fact))
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

    /**
     * Заглушка, пока сервис с фактами не работает
     */
    private suspend fun getFact(): FactDto {
        delay(1000)
        return FactDto("Какой-то факт")
    }

    private suspend fun getImage(): ImageDto {
        return catsImageService.getCatImage()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.destroy()
    }
}