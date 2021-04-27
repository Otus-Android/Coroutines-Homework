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

class CatsViewModel constructor(private val catsRemoteDataSource: CatsRemoteDataSource) : ViewModel() {

    private val _catInfo: MutableLiveData<CatInfo> = MutableLiveData()
    val catInfo: LiveData<CatInfo> = _catInfo

    private val _showServerError: MutableLiveData<Boolean> = MutableLiveData()
    val showServerError: LiveData<Boolean> = _showServerError

    private val _showErrorDialog: MutableLiveData<String> = MutableLiveData()
    val showErrorDialog: LiveData<String> = _showErrorDialog

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        if (e is SocketTimeoutException) _showServerError.value = true
        else _showErrorDialog.value = e.message.orEmpty()
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val requestFact = presenterScope.async { catsRemoteDataSource.getCatFact() }
            val requestImage = presenterScope.async { catsRemoteDataSource.getCatImage() }
            val responseFact = requestFact.await()
            val responseImage = requestImage.await()
            if (responseFact is Result.Success && responseImage is Result.Success) {
                _catInfo.value = CatInfo(responseFact.data, responseImage.data.getImageUrl())
            } else {
                CrashMonitor.trackWarning()
                requestFact.cancel()
                requestImage.cancel()
            }
        }
    }
}