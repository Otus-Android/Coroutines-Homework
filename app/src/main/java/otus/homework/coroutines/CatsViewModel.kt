package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatsRemoteDataSource
import otus.homework.coroutines.api.Result
import java.net.SocketTimeoutException

class CatsViewModel(private val catsRemoteDataSource: CatsRemoteDataSource) : ViewModel() {

    private val _catInfo: MutableLiveData<Result<CatInfo>> = MutableLiveData()
    val catInfo: LiveData<Result<CatInfo>> = _catInfo

    private val _showServerError: MutableLiveData<Boolean> = MutableLiveData()
    val showServerError: LiveData<Boolean> = _showServerError

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        if (e is SocketTimeoutException) _showServerError.value = true
        else _catInfo.value = Result.Error(e)
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val requestCatInfo = presenterScope.async { catsRemoteDataSource.getCatInfo() }
            val responseCatInfo = requestCatInfo.await()
            _catInfo.value = responseCatInfo
        }
    }
}