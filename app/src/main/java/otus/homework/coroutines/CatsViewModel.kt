package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsPresenter.Companion.SOCKET_TIMEOUT_EXCEPTION_MESSAGE
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catFactService: CatsService,
    private val randomCatService: RandomCatService
) : ViewModel() {

    private var job: Job? = null
    private val result = MutableLiveData<Result>()

    fun getResult(): LiveData<Result> = result

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
        val errorMessage = when (exception) {
            is SocketTimeoutException -> SOCKET_TIMEOUT_EXCEPTION_MESSAGE
            else -> exception.message
        }
        result.value = Result.Error(errorMessage = errorMessage)
    }

    init {
        onInitCompleted()
    }

    fun onInitCompleted() {
        job = viewModelScope.launch(CoroutineName("CatsCoroutine") + exceptionHandler) {
            val deferredFact = async { catFactService.getCatFact() }
            val deferredImage = async { randomCatService.getRandomCatImage() }

            val fact = deferredFact.await().fact
            val image = deferredImage.await().catImageUrl

            val catInfoValue = CatInfo(
                catFactText = fact,
                catImageUrl = image
            )

            result.value = Result.Success(data = catInfoValue)
        }
    }
}