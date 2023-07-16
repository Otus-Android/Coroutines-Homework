package otus.homework.coroutines.presentation.mvp

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.StringProvider
import otus.homework.coroutines.utils.coroutines.Dispatcher
import otus.homework.coroutines.utils.coroutines.PresenterScope
import java.net.SocketTimeoutException

/**
 * Презентер получения информации о случайном коте
 *
 * @param repository репозиторий информации о кошке
 * @param stringProvider поставщик строковых значений
 * @param dispatcher обертка получения `coroutine dispatcher`
 */
class CatsPresenter(
    private val repository: CatRepository,
    private val stringProvider: StringProvider,
    dispatcher: Dispatcher
) {

    private var _catsView: ICatsView? = null

    private val scope = PresenterScope(dispatcher)

    private var job: Job? = null

    /** Получить информацию о случайном коте */
    fun getRandomCat() {
        if (job?.isActive == true) {
            _catsView?.warn(message = stringProvider.getString(R.string.active_request_warning))
            return
        }

        job = scope.launch {
            try {
                val catInfo = repository.getCatInfo()
                _catsView?.populate(catInfo)
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    _catsView?.warn(message = stringProvider.getString(R.string.timeout_server_error))
                } else {
                    CrashMonitor.trackWarning(e)
                    _catsView?.warn(message = e.messageOrDefault())
                }
            }
        }
    }

    private fun Exception.messageOrDefault() =
        this.message ?: stringProvider.getString(R.string.default_request_error)

    /** Подключить обработчик информации о кошке [ICatsView] */
    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    /** Отключить обработчик информации о кошке [ICatsView] */
    fun detachView() {
        _catsView = null
        job?.cancel()
        job = null
    }
}