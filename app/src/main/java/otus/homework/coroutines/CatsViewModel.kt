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

    val resultDataValue: LiveData<Result<Any>>
        get() = _resultDataValue

    private val _resultDataValue = MutableLiveData<Result<Any>>()


    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(Exception(exception))
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {

            val picResult =
                async { catsRepository.getCatPic() }

            val factResult = async {
                catsRepository.getCatFact()
            }

            _resultDataValue.value = picResult.await()
            _resultDataValue.value = factResult.await()
        }
    }

}