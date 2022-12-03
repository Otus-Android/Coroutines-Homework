package otus.homework.coroutines

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {


    private var getCatFact: Job? = null
    private val catsData: MutableLiveData<Result<CatsData>> = MutableLiveData()
    val _catsData: LiveData<Result<CatsData>> = catsData
    fun onInitComplete() {
        getCatFact = viewModelScope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getImage() }
                catsData.value = Result.Success(CatsData(fact.await().text, image.await().file))
            } catch (e: java.net.SocketTimeoutException) {
                catsData.value = Result.Error(e)
                CrashMonitor.trackWarning(e.message)
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val catsService = DiContainer()
                CatsViewModel(
                    catsService.service
                )
            }
        }
    }

    override fun onCleared() {
        getCatFact?.cancel("App closed")
        super.onCleared()
    }
}