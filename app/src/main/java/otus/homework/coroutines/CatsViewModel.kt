package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Error
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CatsViewModel( private val catsService: CatsService): ViewModel() {

    private val coroutineException = CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning(throwable, CatsPresenter.TAG)
        }+ CoroutineName("CatsCoroutine")

    private val dispatcher = Dispatchers.IO + SupervisorJob()

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
       viewModelScope.launch(coroutineException + Dispatchers.IO) {
           try {
               val fact = async(dispatcher) { catsService.getCatFact() }
               val img = async(dispatcher) { catsService.getCatimg("https://aws.random.cat/meow") }
               _catsView!!.populateForViewModel(Result.Success(fact.await(), img.await()))
           } catch (e: CancellationException) {
               Log.d("CoroutineExaption", e.toString())
           } catch (e: java.net.SocketTimeoutException) {
               _catsView!!.populateForViewModel(Result.Error(e))
           }
        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }
    companion object {
        val TAG = "CatsPresenter"
    }
}