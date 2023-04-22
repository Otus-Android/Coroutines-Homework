package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import otus.homework.coroutines.CatsService
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.view.Result

class CatViewModel(private val catsService: CatsService) : ViewModel() {

    private val _catUiState =
        MutableStateFlow<Result>(Result.Success(CatModel(null, null)))
    val catUiState: StateFlow<Result> = _catUiState


    init {
        getCat()
    }

    fun getCat() {
        viewModelScope.launch(Dispatchers.IO) {
            supervisorScope {
                val cat = async { catsService.getCatFact() }
                val pictureMeow = async {
                    catsService.getPicture(url = "https://random.dog/woof.json") }
//                    catsService.getPicture(url = "https://aws.random.cat/meow")}

                try {
                    _catUiState.value = Result.Success(CatModel(cat.await().fact, pictureMeow.await().file))
                } catch (exception: Exception) {
                    _catUiState.value = Result.Error(exception)
                }
            }
        }
    }
}