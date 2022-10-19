package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CatsViewModel: ViewModel() {

    private val _catsLiveData = MutableLiveData<Result<Fact>>()
    val catsLiveData: LiveData<Result<Fact>> = _catsLiveData

    fun onInitComplete(catsService: CatsService, awsService: CatsService) {

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning()
            _catsLiveData.postValue(Result.Error(null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val fact = catsService.getCatFact()
                fact.imgUrl = awsService.getCatPicture()
                _catsLiveData.postValue(Result.Success(fact))
            }
        }
    }
}