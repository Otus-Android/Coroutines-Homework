package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.DiContainer
import otus.homework.coroutines.domain.CatModel
import otus.homework.coroutines.domain.Result

class CatViewModel: ViewModel(){

    private val catsService = DiContainer().service

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> get() = _result

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _result.value = Result.Error(throwable)
    }

    fun onInitComplete() {
        val deferredFact = viewModelScope.async { catsService.getCatFact() }

        val deferredImage = viewModelScope.async { catsService.getCatImage() }

        viewModelScope.launch(exceptionHandler) {
            val catModel = CatModel(deferredFact.await(), deferredImage.await().first())
            _result.postValue(Result.Success(catModel))
        }
    }
}