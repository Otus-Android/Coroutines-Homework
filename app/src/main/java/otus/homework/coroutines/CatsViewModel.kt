package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class CatsViewModel(val catsService: CatsService) : ViewModel() {
    private var job: Job? = null
    private var result = MutableLiveData<Result>()

    sealed class Result {
        data class Success(val factAndPicture: FactAndPicture) : Result()
        data class Error(val message: String) : Result()
    }

    fun getCatsFact() {
        var picture: Picture? = null
        var fact: Fact? = null

        job = viewModelScope.launch(Dispatchers.Main + CoroutineName("v1coroutine")) {
            try {
                coroutineScope {
                    val one = async {
                        fact = catsService.getCatFact()
                    }
                    val two = async {
                        picture = catsService.getCatPicture()
                    }

                    awaitAll(one, two)
                    result.value = Result.Success(FactAndPicture(fact!!, picture!!))
                }
            } catch (e: java.net.SocketTimeoutException) {
                result.value = Result.Error("Таймаут")
            }
        }
    }

    fun getObservableData(): MutableLiveData<Result> {
        return result
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}