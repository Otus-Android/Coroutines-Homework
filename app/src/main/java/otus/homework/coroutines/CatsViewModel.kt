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
        if (throwable is java.net.SocketTimeoutException) {
            _catsView?.callOnErrorSocketException()
        } else{
            CrashMonitor.trackWarning(throwable, CatsPresenter.TAG)
        }
    } + CoroutineName("CatsCoroutine")


    private var _catsView: ICatsView? = null

    fun onInitComplete() {
       viewModelScope.launch(coroutineException + Dispatchers.IO) {
                val fact =  catsService.getCatFact()
                val img =  catsService.getCatimg("https://aws.random.cat/meow")
                _catsView!!.populateForViewModel(Result.Success(fact,img))
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