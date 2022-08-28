package otus.homework.coroutines.presentation

import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.network.CatImageService
import otus.homework.coroutines.network.CatsService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) : ViewModel() {

    private val _resultLiveData = MutableLiveData<Result>()
    val resultLiveData: LiveData<Result> = _resultLiveData

    fun onInitComplete() {
        _resultLiveData.value = Result.Loading(true)

        viewModelScope.launch {
            supervisorScope {
                try {
                    val catFactDeferred = async() { catsService.getCatFact() }
                    val catImageDeferred = async() { catImageService.getCatImage() }

                    val fact = catFactDeferred.await()
                    val image = catImageDeferred.await()

                    val catModel = CatModel(fact, image)
                    _resultLiveData.value = Result.Success(catModel)

                } catch (e: Exception) {
                    _resultLiveData.value = when (e) {
                        is SocketTimeoutException -> {
                            Result.Error("Не удалось получить ответ от сервера")
                        }
                        is RuntimeException -> {
                            Result.Error(e.message)
                        }
                        else -> {
                            CrashMonitor.trackWarning()
                            Result.Error(null)
                        }
                    }
                } finally {
                    _resultLiveData.value = Result.Loading(false)
                }
            }
        }
    }

    class Factory(
        private val catsService: CatsService,
        private val catImageService: CatImageService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                return CatsViewModel(catsService, catImageService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}