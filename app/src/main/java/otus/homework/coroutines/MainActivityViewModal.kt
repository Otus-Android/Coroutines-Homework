package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainActivityViewModal(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
): ViewModel() {

    private val _state = MutableLiveData<ResponseResult<CatModal>>()
    val state: LiveData<ResponseResult<CatModal>> get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
        _state.value = throwable.let{ ResponseResult.Error(it) }
    }
    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            val catsFact = catsFactService.getCatFact()
            val catsImage = catsImageService.getImage().first()
            _state.value = ResponseResult.Success(CatModal(
                catsFact,
                catsImage
            ))
        }
    }
}