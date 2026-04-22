package otus.homework.coroutines.presentation

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Error
import otus.homework.coroutines.model.Loading
import otus.homework.coroutines.model.Success
import otus.homework.coroutines.network.CatsService
import java.net.SocketTimeoutException

/**
 * Presenter для экрана с котами.
 * Реализовано согласно ТЗ по Coroutines: использование собственного Scope (presenterScope),
 * параллельный запуск запросов через async (дополнительно для ||), отмена в onStop.
 */
class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private var fetchJob: Job? = null

    /**
     * CoroutineScope Presenter.
     * Согласно заданию включает CoroutineName("CatsCoroutine") и Dispatchers.Main.
     */
    private val presenterScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine"))

    /**
     * Запускает получение данных (факт + картинка).
     * Параллельность обеспечена async { ... }.await().
     */
    fun onInitComplete() {
        fetchJob?.cancel()

        fetchJob = presenterScope.launch {
            _catsView?.populate(Loading)
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsService.getCatImage().first() }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                _catsView?.populate(Success(CatsData(fact, image)))
            } catch (_: SocketTimeoutException) {
                _catsView?.populate(Error("Не удалось получить ответ от сервера"))
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.populate(Error(e.message ?: "Unknown error"))
            }
        }
    }

    /**
     * Отмена всех корутин Presenter.
     * Исправлено по ревью: отменяем только текущую задачу, а не весь Scope,
     * чтобы при возврате на экран презентер продолжал работать.
     */
    fun onStop() {
        fetchJob?.cancel()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}
