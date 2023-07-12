package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) : ViewModel() {

    private var _stateLiveData = MutableLiveData<Result<CatFactWithImage>>()
    val stateLiveData: LiveData<Result<CatFactWithImage>> get() = _stateLiveData

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _stateLiveData.value = Result.Error(Error(throwable.message))
            CrashMonitor.trackWarning("", throwable)
        }) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catImageService.getCatImage() }
                val catInfo = CatFactWithImage(fact.await().fact, image.await().url)
                _stateLiveData.value = Result.Success(catInfo)
            } catch (e: Exception) {
                _stateLiveData.value = Result.Error(Error("Не удалось получить ответ от сервера"))
                CrashMonitor.trackWarning("", e)
            }
        }
    }
}