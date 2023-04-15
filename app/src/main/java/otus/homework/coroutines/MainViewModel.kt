package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatsImageService
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.models.CatInfoDto
import otus.homework.coroutines.utils.CrashMonitor
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _catInfo = MutableLiveData<Result>()
    val catInfo: LiveData<Result> = _catInfo

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable.message.toString())
        _catInfo.value = Result.Error(throwable, (_catInfo.value as? Result.Success)?.data)
    }

    init {
        updateData()
    }

    private fun updateData() {
        val responseFact = viewModelScope.async { catsService.getCatFact() }
        val responseImage = viewModelScope.async { catsImageService.getCatImage() }

        viewModelScope.launch(coroutineExceptionHandler) {
            val text = fetchData { responseFact.await().body()?.fact }
            val imageUrl = fetchData { responseImage.await().body()?.get(0)?.url }
            _catInfo.value = Result.Success(
                CatInfoDto(
                    text = text,
                    imageUrl = imageUrl,
                )
            )
        }
    }

    private inline fun <T> fetchData(action: () -> T): T? {
        return try {
            action.invoke()
        } catch (ex: SocketTimeoutException) {
            _catInfo.value = Result.Error(ex, (_catInfo.value as? Result.Success)?.data)
            null
        }
    }
}

sealed class Result {
    data class Success(val data: CatInfoDto) : Result()
    data class Error(val throwable: Throwable?, val oldData: CatInfoDto?) : Result()
}