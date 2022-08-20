package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel() : ViewModel() {

    private var _catsView: ICatsView? = null

    private val catsService = DiContainer().service
    private val catsServiceMeow = DiContainer().serviceMeow

    val state = MutableLiveData<Result<FactAndPicture>>()

    fun onInitComplete() {
        viewModelScope.launch(CoroutineName("CatsCoroutine") + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("CoroutineExceptionHandler", "${throwable.message}")
        }) {
            try {
                val fact = requestCatsFact()
                val picture = requestCatsPicture()
               state.postValue(Result.Success(FactAndPicture(fact, picture)))
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    state.value = Result.Error("Не удалось получить ответ от сервера")
                } else {
                    CrashMonitor.trackWarning()
                    withContext(Dispatchers.Main) {
                        state.value = Result.Error(e.message.toString())
                    }
                }
            }
        }
    }

    private suspend fun requestCatsFact(): Fact {
        val async = viewModelScope.async {
                return@async catsService.getCatFact()
        }
        return async.await()
    }

    private suspend fun requestCatsPicture(): Picture {
        val async = viewModelScope.async {
           return@async catsServiceMeow.getCatRandomPicture()
        }
        return async.await()
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        viewModelScope.cancel()
    }

}