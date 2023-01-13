package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val catsService: CatsService,
    private val meowService: MeowService,
) : ViewModel() {

    private val _viewObject = MutableLiveData<ResultOf<CatsVO>>()
    val viewObject: LiveData<ResultOf<CatsVO>> = _viewObject

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is SocketTimeoutException -> {
                Log.e(TAG, "Не удалось получить ответ от сервера", throwable)
                _viewObject.value = ResultOf.Failure(
                    message = "Не удалось получить ответ от сервера",
                    throwable = throwable
                )
            }
            else -> {
                Log.e(TAG, "Что-то пошло не так", throwable)
                CrashMonitor.trackWarning()
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _viewObject.value = ResultOf.Success(
                value = withContext(Dispatchers.IO) {
                    val fact = catsService.getCatFact()
                    val meow = meowService.getCatImage()
                    CatsVO(fact = fact.fact, imageUrl = meow.imageUrl)
                }
            )
        }
    }

    companion object {
        private const val TAG = "CatsViewModel"
    }
}