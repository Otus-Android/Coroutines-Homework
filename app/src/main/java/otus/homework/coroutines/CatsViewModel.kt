package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val factsService: FactsService,
) : ViewModel() {
    private val _catsViewFlow = MutableStateFlow<Result<out CatFact>?>(null)
    val catsFlow = _catsViewFlow.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, ex ->
        CrashMonitor.trackWarning(ex)
        _catsViewFlow.value = Result.Error(ex.message, ex)
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() = viewModelScope.launch(SupervisorJob() + exceptionHandler) {
            try {
                val facts = async {
                    factsService.getFact()
                }
                val cats = async {
                    catsService.getCats()
                }
                val value = CatFact(facts.await().text, cats.await()[0].url)
                _catsViewFlow.emit(Result.Success(value))
            } catch (ex: SocketTimeoutException) {
                _catsViewFlow.value = Result.Error("Не удалось получить ответ от сервером", ex)
            }
        }

}