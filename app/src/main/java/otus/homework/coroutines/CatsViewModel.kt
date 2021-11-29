package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsServiceImg: CatsServiceImg)
    : ViewModel() {

    val catsData: LiveData<Result> by lazy { _catsData }
    private val _catsData: MutableLiveData<Result> by lazy {
        MutableLiveData<Result>()
    }

    fun onInitComplete() {
        viewModelScope.launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.logThrowable(throwable)
            throwable.message?.let {
                _catsData.value = Error(it)
            }
        }) {
            try {
                val response =
                    async {
                        catsService.getCatFact()
                    }

                val responseImg = async {
                    catsServiceImg.getCatImage()
                }

                val resFact = response.await()
                val resImage = responseImg.await()

                _catsData.value = Success(CatsData(
                    resFact.text,
                    resImage.file
                ))
            } catch (e: SocketTimeoutException) {
               _catsData.value = NetworkError
            }
        }
    }
}