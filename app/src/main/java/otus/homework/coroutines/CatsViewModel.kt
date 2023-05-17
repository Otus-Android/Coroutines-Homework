package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.Fact
import otus.homework.coroutines.data.Pic
import otus.homework.coroutines.network.CatsRepository
import otus.homework.coroutines.network.Result
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val catsRepository: CatsRepository
) : ViewModel() {

    val factDataValue: LiveData<Result<Fact>>
        get() = _factDataValue

    private val _factDataValue = MutableLiveData<Result<Fact>>()

    val picDataValue: LiveData<Result<Pic>>
        get() = _picDataValue

    private val _picDataValue = MutableLiveData<Result<Pic>>()

    val errorDataValue: LiveData<String>
        get() = _errorDataValue

    private val _errorDataValue = MutableLiveData<String>()

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(Exception(exception))
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            val factResult = async {
                catsRepository.getCatFact()
            }
            _factDataValue.value = factResult.await()
        }

        viewModelScope.launch(handler) {
            val picResult =
                async { catsRepository.getCatPic() }

            _picDataValue.value = picResult.await()
        }
    }

fun isError(exception: Exception) {
    when (exception) {
        is SocketTimeoutException -> {
            _errorDataValue.value = "Не удалось получить ответ от сервера"
        }
        else -> {
            _errorDataValue.value = exception.message.toString()
            CrashMonitor.trackWarning(exception)
        }
    }
}


}