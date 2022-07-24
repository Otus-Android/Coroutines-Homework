package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _state: MutableLiveData<Result<CatsViewState>> = MutableLiveData()
    val state: LiveData<Result<CatsViewState>> = _state

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable)
        }

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val catFact = async { catsService.getCatFact() }
                val catPhoto = async { catsService.getRandomCatPhoto() }

                val newState = CatsViewState(
                    fact = catFact.await().text,
                    fileUrl = catPhoto.await().file
                )
                _state.postValue(Result.Success(newState))
            } catch (e: SocketTimeoutException) {
                _state.postValue(Result.Error("Не удалось получить ответ от сервером"))
            } catch (e: Exception) {
                e.message?.let {
                    _state.postValue(Result.Error(it))
                }
                CrashMonitor.trackWarning(e)
            }
        }
    }


    class CatsViewModelProviderFactory(
        private val catsService: CatsService
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsService = catsService) as T
        }
    }
}