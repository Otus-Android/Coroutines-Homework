package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

//1. Поменять возвращаемый тип в `CatsService` и добавить модификатор `suspend`
//2. Переписать логику в презентере с `Callback` на корутины и `suspend` функции
//3. Реализовать свой скоуп: PresenterScope с `MainDispatcher` и CoroutineName("CatsCoroutine") в качестве элементов контекста
//4. Добавить обработку исключений через try-catch. В случае `java.net.SocketTimeoutException` показываем Toast с текстом "Не удалось получить ответ от сервером". В остальных случаях логируем исключение в `otus.homework.coroutines.CrashMonitor` и показываем Toast с `exception.message`
//5. Не забываем отменять Job в `onStop()`
class CatsPresenter(
    private val catsService: CatsService
) {
    private val presenterScope: CoroutineScope =
        CoroutineScope(Job() + Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val response = catsService.getCatFact()
                _catsView?.populate(response)
            } catch (e: Exception) {
                if (e == java.net.SocketTimeoutException()) {
                    _catsView?.showToast("Не удалось получить ответ от сервером")
                } else {
                    CrashMonitor.trackWarning()
                    e.message?.let { _catsView?.showToast(it) }
                }
            }
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