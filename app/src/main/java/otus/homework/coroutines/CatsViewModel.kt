package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class CatsViewModel(val catsService: CatsService) : ViewModel() {
    private var job: Job? = null

    private val _result= MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    sealed class Result {
        data class Success(val factAndPicture: FactAndPicture) : Result()
        data class Error(val message: String) : Result()
    }


    private val vmExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
        }

    fun getCatsFact() {
        var picture: Picture? = null
        var fact: Fact? = null

        job = viewModelScope.launch( CoroutineName("v1coroutine") + vmExceptionHandler) {
            try {
                coroutineScope {
                    val one = async {
                        fact = catsService.getCatFact()
                    }
                    val two = async {
                        picture = catsService.getCatPicture()
                    }

                    awaitAll(one, two)

                    _result.value = Result.Success(FactAndPicture(fact!!, picture!!))
                }
            } catch (e: java.net.SocketTimeoutException) {
                _result.value = Result.Error("Таймаут")
            }
        }
    }




}