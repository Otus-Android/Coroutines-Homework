package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.entities.CatEntity

class CatViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _cat = MutableLiveData<Result<CatEntity>>()
    val cat: LiveData<Result<CatEntity>> = _cat

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(CatViewModel::class.java.name, throwable.message ?: "")
        _cat.postValue(Result.Error(throwable.message ?: ""))
    }

    fun onInitComplete() = viewModelScope.launch(exceptionHandler) {

        val catFact = async(Dispatchers.IO) {
            catsService.getCatFact()
        }
        val catPhotoUrl = async(Dispatchers.IO) {
            catsService.getPhotoCat()
        }

        _cat.postValue(Result.Success(CatEntity(catFact.await().text, catPhotoUrl.await().fileUrl)))
    }

    class ViewModelFactory(
        private val catsService: CatsService,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
                return CatViewModel(catsService) as T
            }
            throw IllegalArgumentException("VM not found")
        }
    }
}
