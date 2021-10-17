package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException


class CatViewModel(private val catsService: CatsService) : ViewModel() {

    private val _catModel = MutableLiveData<Result>()
    val catModel: LiveData<Result> get() = _catModel

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SocketTimeoutException -> _catModel.postValue(Result.Error("Не удалось получить ответ от сервера"))
            else -> {
                _catModel.postValue(Result.Error("Ошибка ${exception.message}"))
                CrashMonitor.trackWarning()
            }
        }
    }

    init {
        loadInfo()
    }

    internal fun loadInfo() {
        viewModelScope.launch(exceptionHandler) {
            val fact = async(Dispatchers.IO) {
                catsService.getCatFact()
            }

            val image = async(Dispatchers.IO) {
                catsService.getCatImage()
            }

            val model = Result.Success(CatModel(fact.await(), image.await()))
            _catModel.postValue(model)
        }
    }

}