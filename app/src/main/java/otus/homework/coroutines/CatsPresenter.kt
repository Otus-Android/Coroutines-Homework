package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val job = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private val _getFactState = MutableLiveData<Result?>()
    val getFactState: LiveData<Result?>
        get() = _getFactState

    fun onInitComplete() {

        presenterScope.launch {
            try {
                val response = catsService.getCatFact()
                if (response.isSuccessful && response.body() != null) {
                    _catsView?.populate(response.body()!!.first())
                } else {
                    CrashMonitor.trackWarning(response.errorBody()?.string())
                }
            } catch (ex: Exception) {
                _getFactState.value = Error(ex)
                CrashMonitor.trackWarning(ex.message.toString())
            }
        }
    }

    fun onMessageShown() {
        _getFactState.value = null
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}

sealed class Result
data class Error(val t: Throwable?) : Result()
object Empty : Result()
data class Success(val data: Fact) : Result()