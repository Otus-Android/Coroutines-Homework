package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.ExceptionWrapper
import otus.homework.coroutines.ExceptionWrapperImpl
import otus.homework.coroutines.model.CatItem
import otus.homework.coroutines.services.CatImageService
import otus.homework.coroutines.services.CatsService

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: CatImageService
) : ViewModel(), ExceptionWrapper by ExceptionWrapperImpl() {

    private val _result = MutableLiveData<Result<CatItem>>()
    val result: LiveData<Result<CatItem>> get() = _result

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler { error ->
            _result.value = Result.Error(error)
        }) {
            val facts = async { catsService.getCatFact() }
            val image = async { imageService.getCatImage() }
            _result.value = Result.Success(CatItem(image.await(), facts.await()))
        }
    }

}