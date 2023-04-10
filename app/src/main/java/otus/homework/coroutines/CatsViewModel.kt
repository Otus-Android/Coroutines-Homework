package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import otus.homework.coroutines.network.CatsRepository
import otus.homework.coroutines.network.Result
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val catsRepository: CatsRepository
) : ViewModel() {

    val populateDataValue = MutableLiveData<PopulateData>()

    val errorDataValue = MutableLiveData<String>()

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(Exception(exception))
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            val factResult = catsRepository.getCatFact()
            val picResult = catsRepository.getCatPic()


            if (factResult is Result.Success && picResult is Result.Success) {
                populateDataValue.value = PopulateData(factResult.data.fact, picResult.data.url)
            } else if (factResult is Result.Error) {
                isError(factResult.exception)
            } else if (picResult is Result.Error) {
                isError(picResult.exception)
            }
        }
    }

    private fun isError(exception: Exception) {
        when (exception) {
            is SocketTimeoutException -> {
                errorDataValue.value = "Не удалось получить ответ от сервера"
            }
            else -> {
                errorDataValue.value = exception.message.toString()
                CrashMonitor.trackWarning(exception)
            }
        }
    }


}