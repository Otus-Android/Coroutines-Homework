package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsService
import otus.homework.coroutines.logException
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.view.Result

class CatViewModel(private val catsService: CatsService) : ViewModel() {

    private val _catUiState =
        MutableStateFlow<Result>(Result.Success(CatModel(null, null)))
    val catUiState: StateFlow<Result> = _catUiState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        logException(this, exception.message.toString())
        _catUiState.value = Result.Error(exception)
    }

    init {
        getCat()
    }

    fun getCat() {

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val cat = catsService.getCatFact()
            val pictureMeow = catsService.getPicture(url = "https://aws.random.cat/meow")

            _catUiState.value = Result.Success(CatModel(cat.fact, pictureMeow.file))
        }
    }
}