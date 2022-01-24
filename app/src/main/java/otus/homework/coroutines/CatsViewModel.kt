package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import otus.homework.coroutines.dto.Fact
import otus.homework.coroutines.other.Resource
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    companion object {
        const val TAG = "CatsViewModel"
    }

    private val _fact = MutableLiveData<Fact>()
    val fact: LiveData<Fact> = _fact

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
    }

    fun onButtonNewFactPressed() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            getFacts().collect { result ->
                ensureActive()
                when (result) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let {
                                _fact.postValue(it)
                            }
                        }
                    }
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            result.message?.let { Log.e(TAG, it) }
                            _toastMessage.postValue("Не удалось получить ответ от сервера")
                        }
                    }
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            getImageUrl().collect { result ->
                ensureActive()
                when (result) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let {
                                _imageUrl.postValue(it.file)
                            }
                            _toastMessage.postValue(null)
                        }
                    }
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            result.message?.let { Log.e(TAG, it) }
                            _toastMessage.postValue("Не удалось получить ответ от сервера")
                        }
                    }
                }
            }
        }
    }

    private suspend fun getFacts() = flow() {
        try {
            emit(Resource.Loading())
            val fact = catsService.getCatFact()
            emit(Resource.Success(fact))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error(e.message))
        }
    }

    private suspend fun getImageUrl() = flow() {
        try {
            emit(Resource.Loading())
            val imageUrl = catsService.getCatImage()
            emit(Resource.Success(imageUrl))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error(e.message))
        }
    }

}