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
            when (val result = catsRepository.getCatFact()) {
                is Result.Error -> {
                    when (result.exception) {
                        is SocketTimeoutException -> {
                            _catsView?.error("Не удалось получить ответ от сервера")
                        }
                        else -> {
                            _catsView?.error(result.exception.message.toString())
                            CrashMonitor.trackWarning(result.exception)
                        }
                    }
                }
                is Result.Success -> {
                    _catsView?.populate(result.data)
                }
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