package otus.homework.coroutines

import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsRepository: CatsRepository
) {

    private var _catsView: ICatsView? = null

    private val scope = PresenterScope()

    fun onInitComplete() {

        scope.launch {

            val factResult = catsRepository.getCatFact()
            val picResult = catsRepository.getCatPic()

            if (factResult is Result.Success && picResult is Result.Success) {
                _catsView?.populate(PopulateData(factResult.data.fact, picResult.data.url))
            } else if (factResult is Result.Error) {
                isError(factResult.exception)
            } else if (picResult is Result.Error) {
                isError(picResult.exception)
            }
        }

    }

    private fun isError(exception: Exception) {
        when (exception) {
            is SocketTimeoutException -> {
                _catsView?.error("Не удалось получить ответ от сервера")
            }
            else -> {
                _catsView?.error(exception.message.toString())
                CrashMonitor.trackWarning(exception)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        if (scope.isActive)
            scope.cancel()
    }
}

data class PopulateData(val factText: String, val imageCat: String)