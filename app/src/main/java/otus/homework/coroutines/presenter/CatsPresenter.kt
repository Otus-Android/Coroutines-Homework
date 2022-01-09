package otus.homework.coroutines.presenter
import kotlinx.coroutines.*
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.utils.CrashMonitor
import java.lang.Exception
import java.net.SocketTimeoutException

const val message = "Не удалось получить ответ от сервером"
class CatsPresenter(
    private val catsService: CatsService
) {
    private var dataJob: Job? = null
    private val handler = CoroutineExceptionHandler{
        context, exception -> CrashMonitor.trackWarning(exception = exception)
    }
    private val presenterScope = CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            if(dataJob?.isActive == true) return@launch
            dataJob = launch(handler) {
                _catsView?.showLoading()
                delay(10000)
                try {
                    val catFact = catsService.getCatFact()
                    val imageResource = catsService.getImageResource()
                    _catsView?.populate(catFact, imageResource)
                    _catsView?.hideLoading()
                } catch (ex: Exception) {
                    _catsView?.hideLoading()
                    when(ex) {
                        is SocketTimeoutException -> {_catsView?.showToast(message)}
                        else -> { CrashMonitor.trackWarning(ex) }
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
    fun onStopCoroutine() {
        dataJob?.cancel()
        _catsView?.hideLoading()
    }
}