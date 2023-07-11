package otus.homework.coroutines.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.CatsModel
import otus.homework.coroutines.network.CatsImageService
import otus.homework.coroutines.network.CatsService
import otus.homework.coroutines.network.DiContainer
import otus.homework.coroutines.network.models.Fact
import otus.homework.coroutines.utils.Result
import otus.homework.coroutines.utils.Result.Error
import otus.homework.coroutines.utils.Result.Success
import otus.homework.coroutines.utils.orEmpty

class CatsViewModel : ViewModel() {

    private val _result = MutableStateFlow<Result>(
        Success(CatsModel(catsImage = emptyList(), fact = Fact()))
    )
    val result = _result.asStateFlow()
    private val diContainer = DiContainer()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            _result.emit(Error(error = exception))
        }
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val catsFact = async {
                try {
                    (diContainer.choiceUrlForRetrofit(isFact = true) as? CatsService)
                        ?.getCatFact()
                        .orEmpty()
                } catch (e: Throwable) {
                    Fact()
                }
            }
            val catsImage = async {
                try {
                    (diContainer.choiceUrlForRetrofit(isFact = false) as? CatsImageService)
                        ?.getCatImageUrl()
                        .orEmpty()
                } catch (e: Throwable) {
                    emptyList()
                }
            }
            _result.emit(
                Success(
                    CatsModel(
                        catsImage = catsImage.await(),
                        fact = catsFact.await()
                    )
                )
            )
        }
    }
}
