package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.data.DiContainer
import otus.homework.coroutines.domain.CatImage
import otus.homework.coroutines.domain.CatModel
import otus.homework.coroutines.domain.Fact
import otus.homework.coroutines.domain.Result
import java.net.SocketTimeoutException

class CatViewModel: ViewModel(){

    private val catsService = DiContainer().service

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> get() = _result

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _result.value = Result.Error(throwable)
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {

            val deferredFact = withContext(Dispatchers.IO) {
                    try {
                        catsService.getCatFact()
                    } catch (e: SocketTimeoutException) {
                        _result.value = Result.Error(e)
                    }
                }

            val deferredImage = withContext(Dispatchers.IO) {
                    try {
                        catsService.getCatImage()
                    } catch (e: SocketTimeoutException) {
                        _result.value = Result.Error(e)
                    }
                }

            if (deferredFact != Unit && deferredImage != Unit) {
                val catModel =
                    CatModel(
                        deferredFact as Fact,
                        (deferredImage as List<CatImage>).first()
                    )
                _result.value = Result.Success(catModel)
            }
        }
    }
}