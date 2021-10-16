package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val catData: LiveData<Result> get() = _catData
    private val _catData = MutableLiveData<Result>()

    fun requestCats() {

        viewModelScope.launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is SocketTimeoutException ->
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            _catData.postValue(Error(throwable))
                        }
                    }
                else -> viewModelScope.launch(Dispatchers.Default) {
                    _catData.postValue(Error(throwable))
                    CrashMonitor.trackWarning()
                }
            }
        }) {
            val cats = catsService.getCatFact()
            val picture = catsService.getCatPicture()
            withContext(Dispatchers.Main) {
                _catData.postValue(Success(CatModel(cats, picture)))
            }
        }
    }
}