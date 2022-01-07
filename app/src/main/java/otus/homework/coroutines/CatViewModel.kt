package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CatViewModel(private val catsService: CatsService) : ViewModel() {

    private val _data: MutableLiveData<Result<CatData>> = MutableLiveData()
    internal val data: LiveData<Result<CatData>>
        get() = _data

    fun refresh() {
        viewModelScope.launch {
            supervisorScope {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getCatImage() }

                try {
                    val data = CatData(fact.await(), image.await())
                    _data.value = Result.Success(data)

                } catch (ex: Exception) {
                    val message = when (ex) {
                        is SocketTimeoutException -> "Failed to get a response from the server"
                        is UnknownHostException -> "Check internet connection"
                        else -> ex.message ?: ""
                    }

                    CrashMonitor.trackWarning()
                    _data.value = Result.Error(message)
                }
            }
        }
    }
}


class CatViewModelFactory(
    private val catsService: CatsService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CatViewModel(catsService) as T
}