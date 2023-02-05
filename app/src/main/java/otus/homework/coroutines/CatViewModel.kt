package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatViewModel(private val catsService: CatsService) : ViewModel() {

    private val _catUiState =
        MutableStateFlow<CatUiState>(CatUiState.Success(CatModel(null, null)))
    val catUiState: StateFlow<CatUiState> = _catUiState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        logException(this, exception.message.toString())
        _catUiState.value = CatUiState.Error(exception)
    }

    init {
        getCat()
    }

    fun getCat() {

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val cat = catsService.getCatFact()
            val pictureMeow = catsService.getPicture(url = "https://aws.random.cat/meow")

            _catUiState.value = CatUiState.Success(CatModel(cat.fact, pictureMeow.file))
        }
    }
}