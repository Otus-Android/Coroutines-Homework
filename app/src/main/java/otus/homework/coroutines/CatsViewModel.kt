package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val picsService: PicsService
) : ViewModel() {

    private val _dataState = MutableStateFlow<Result<CatArticle>>(Result.Loading)
    val dataState: StateFlow<Result<CatArticle>> = _dataState

    private var job: Job? = null

    init {
        loadData()
    }

    fun loadData() {
        job?.cancel()
        job = viewModelScope.launch(
            CoroutineExceptionHandler { _, e ->
                _dataState.value = Result.Error(e)
            }
        ) {
            _dataState.value = Result.Loading
            _dataState.value = Result.Success(
                data = CatArticle(
                    fact = async { catsService.getCatFact() }.await(),
                    pic = async { picsService.getPic()[0] }.await()
                )
            )
        }
    }

}