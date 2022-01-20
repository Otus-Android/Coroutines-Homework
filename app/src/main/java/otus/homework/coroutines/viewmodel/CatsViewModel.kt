package otus.homework.coroutines.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.R
import otus.homework.coroutines.facts.FactsService
import otus.homework.coroutines.pictures.PicturesService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val factsService: FactsService,
    private val picsService: PicturesService,
) : ViewModel(), ICatsViewModel {

    private val _state = MutableLiveData<Result<CatModel>>()
    override val state = _state as LiveData<Result<CatModel>>

    private var loadFactsJob: Job? = null

    private val scopeExceptionHandler = CoroutineExceptionHandler { _, ex ->
        when (ex) {
            is SocketTimeoutException -> {
                _state.postValue(Result.Error(resId = R.string.server_not_responding))
            }
            else -> {
                _state.postValue(Result.Error(msg = "${ex.message}"))
                CrashMonitor.trackWarning()
            }
        }
    }

    override fun updateData() {
        loadFactsJob?.cancel()
        loadFactsJob = viewModelScope.launch(scopeExceptionHandler) {
            val fact = async { factsService.getFact() }
            val pic = async { picsService.getPicture() }
            _state.value = Result.Success(CatModel(fact.await().text, pic.await().file))
        }
    }

}
