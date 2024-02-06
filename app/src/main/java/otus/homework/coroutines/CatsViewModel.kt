package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketException

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
            val factDiffered = async { catsService.getCatFact() }
            val picDiffered = async { picsService.getPic()[0] }
            try {
                _dataState.value = Result.Loading
                val (fact, pic) = awaitAll(factDiffered, picDiffered)
                _dataState.value = Result.Success(
                    data = CatArticle(fact = fact as Fact, pic = pic as Pic)
                )
            } catch (e: SocketException) {
                _dataState.value = Result.Error(e)
            }
        }
    }

}