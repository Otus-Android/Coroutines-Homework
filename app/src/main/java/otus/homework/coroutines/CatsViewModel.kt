package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            CrashMonitor.trackWarning()
        }) {
            try {
                _result.value = Result.Success(
                    Cat(
                        fact = catsService.getCatFact(),
                        image = catsService.getCatImage()
                    )
                )
            } catch (exception: UnknownHostException) { //SocketTimeoutException
                _result.value = Result.Error
            }
        }
    }
}