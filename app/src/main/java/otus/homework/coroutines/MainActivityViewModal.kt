package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivityViewModal(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
): ViewModel() {

    private val _state = MutableLiveData<ResponseResult>()
    val state: LiveData<ResponseResult> get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
        _state.value = throwable.let{ ResponseResult.Error(it) }
    }
    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val catsFact = async { catsFactService.getCatFact() }
                val catsImage = async { catsImageService.getImage().first()  }
                _state.value = ResponseResult.Success(CatModal(
                    catsFact.await(),
                    catsImage.await()
                ))
            }catch (e: Exception){
                _state.value = ResponseResult.Error(e)
            }

        }
    }
}