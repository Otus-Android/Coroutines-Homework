package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.CatResponse
import java.net.SocketTimeoutException


class MainViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImage
) : ViewModel() {

    private var job: Job? = null
    private val _responseResult = MutableLiveData<Result>()
    val responseResult : LiveData<Result> = _responseResult

    val exceptionHandler = CoroutineExceptionHandler{_, exception ->
        val error = when(exception){
            is SocketTimeoutException -> "Не удалось получить ответ от сервера"
            else -> exception.message
        }
        _responseResult.postValue(Result.Error(error.toString()))
    }
    init {
        onInitComplete()
    }

    fun onInitComplete() {
        job = viewModelScope.launch(exceptionHandler){
                val catFact = async {catsService.getCatFact()}
                    .await()
                val catImage = async {catsImageService.getCatImage()}
                    .await()
                val catResponse = CatResponse(catFact = catFact, catImage = catImage.elementAt(0).url)
                _responseResult.postValue(Result.Success(response = catResponse))
        }
    }
    companion object{
        class VMFactory (private val catsService: CatsService,
                         private val catsImageService: CatsImage)
            : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(catsService, catsImageService) as T
            }

        }
    }

}
