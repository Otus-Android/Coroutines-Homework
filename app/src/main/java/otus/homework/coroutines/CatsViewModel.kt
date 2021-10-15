package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Error
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CatsViewModel( private val catsService: CatsService): ViewModel() {

        val coroutineException=  CoroutineExceptionHandler { coroutineContext, throwable ->
                Log.d("Exaption", throwable.toString())
            } + CoroutineName("CatsCoroutine")


    private var _catsView: ICatsView? = null

    fun onInitComplete() {
       viewModelScope.launch(coroutineException) {
            try {
                val job = async(Dispatchers.IO) { catsService.getCatFact()}
                val jobImg = async(Dispatchers.IO) { catsService.getCatimg("https://aws.random.cat/meow")}
                doAction(Result.Success(job.await(),jobImg.await()))
            } catch (e: CancellationException) {
                doAction(Result.Error(e))
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.callOnErrorSocketException()
            } catch (e: Exception) {
                doAction(Result.Error(e))
                _catsView?.callOnErrorAnyException(e)
            }
        }
    }

    fun doAction(result: Result) {
        when (result) {
            is Result.Error -> CrashMonitor.trackWarning(result.exception, CatsPresenter.TAG)
            is Result.Success<*, *> -> { _catsView?.populate(FullFact(result.data as Fact,result.data2 as ImageFact))}
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }
    companion object {
        val TAG = "CatsPresenter"
    }
}