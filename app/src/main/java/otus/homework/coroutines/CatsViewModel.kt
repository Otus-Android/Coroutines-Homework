package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsService
) : ViewModel() {

    private var _catsView: ICatsView? = null
    private var presenterScope: CoroutineScope = viewModelScope
    private val catsLiveData = MutableLiveData<Result>()
    val getCatsLiveData: LiveData<Result>
        get() = catsLiveData

    fun onInitComplete() {

        presenterScope.launch(CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }) {
            try {
                val factResponse =
                    withContext(Dispatchers.Default) { catsService.getCatFact() }
                val imageResponse =
                    withContext(Dispatchers.Default) { catsImageService.getCatImage() }

                catsLiveData.value =
                    Result.Success(FactAndImage(factResponse.text, imageResponse.file))

            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        Result.Error(null, R.string.socket_timeout_ex_message)
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        Result.Error(ex.message ?: "", null)
                    }
                }
            }
        }
    }
}