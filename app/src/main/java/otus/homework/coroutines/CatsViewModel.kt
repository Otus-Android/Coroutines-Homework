package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(private val catsRepository: CatsRepository): ViewModel() {
    private var job: Job? = null
    val factData : MutableLiveData<String> = MutableLiveData()
    val imageData : MutableLiveData<String> = MutableLiveData()
    val errorMessage : MutableLiveData<String> = MutableLiveData()

    fun onInitComplete() {
        job = viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                CrashMonitor.trackWarning(throwable)
            }){
            coroutineScope {
                val image = withContext(Dispatchers.Default) { catsRepository.getCatImage() }
                val fact = withContext(Dispatchers.Default) { catsRepository.getCatFact() }

                when (image) {
                    is Result.Success -> {
                        imageData.postValue(image.value.file)
                    }
                    is Result.GenericError -> {
                        errorMessage.postValue(image.error?.message)
                    }
                }
                when (fact) {
                    is Result.Success -> {
                        factData.postValue(fact.value.text)
                    }
                    is Result.GenericError -> {
                        errorMessage.postValue(fact.error?.message)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    class CatsViewModelFactory(private val catsRepository: CatsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CatsViewModel(catsRepository) as T
        }
    }
}