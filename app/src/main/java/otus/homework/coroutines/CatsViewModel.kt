package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {
    private var _catsView: ICatsView? = null
    lateinit var catsService: CatsService
    lateinit var picsService: CatsService

    val result: MutableLiveData<Result<*>> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SocketTimeoutException -> result.postValue(
                Result.Error(
                    null,
                    R.string.failed_response
                )
            )
            else -> result.postValue(Result.Error(null, exception.message))
        }
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        viewModelScope.launch(Dispatchers.Main + handler) {
            try {
                val fact = async {
                    catsService.getCatFact()
                }
                val pic = async {
                    picsService.getPic()
                }

                result.postValue(Result.Success(Pair(fact.await(), pic.await())))
            } catch (e: SocketTimeoutException) {
                result.postValue(Result.Error(null, R.string.failed_response))
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                result.postValue(Result.Error(null, "Ошибка"))
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        viewModelScope.cancel()
        _catsView = null
    }
}