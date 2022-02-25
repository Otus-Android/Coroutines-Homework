package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.dto.Fact
import otus.homework.coroutines.dto.ImageFile
import otus.homework.coroutines.other.Resource
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    companion object {
        const val TAG = "CatsViewModel"
    }

    private val _result = MutableSharedFlow<Resource<Fact>>()
    val result: SharedFlow<Resource<Fact>> = _result

    private val _imageUrl = MutableSharedFlow<Resource<ImageFile>>()
    val imageUrl: SharedFlow<Resource<ImageFile>> = _imageUrl

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
    }

    fun onButtonNewFactPressed() {
        viewModelScope.launch(exceptionHandler) {
            try {
                _result.emit(Resource.Loading())
                val fact = catsService.getCatFact()
                _result.emit(Resource.Success(fact))
            } catch (e: SocketTimeoutException) {
                _result.emit(Resource.Error(e.message))
            }
        }
        viewModelScope.launch(exceptionHandler) {
            try {
                _imageUrl.emit(Resource.Loading())
                val imageUrl = catsService.getCatImage()
                _imageUrl.emit(Resource.Success(imageUrl))
            } catch (e: SocketTimeoutException) {
                _imageUrl.emit(Resource.Error(e.message))
            }
        }
    }
}