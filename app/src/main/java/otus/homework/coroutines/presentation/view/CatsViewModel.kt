package otus.homework.coroutines.presentation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.*
import otus.homework.coroutines.data.CatModel
import otus.homework.coroutines.presentation.PresenterScope
import otus.homework.coroutines.service.CrashMonitor
import otus.homework.coroutines.service.FactsService
import otus.homework.coroutines.service.PicsService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val factsService: FactsService,
    private val picsService: PicsService
): ViewModel() {

    private val _state = MutableLiveData<Result<CatModel>>()
    val state: LiveData<Result<CatModel>>
        get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { context, e ->
        CrashMonitor.trackWarning(e)
    }

    fun loadCats() {
        viewModelScope.launch(exceptionHandler) {
            supervisorScope {
                val facts = async(Dispatchers.IO) {
                    factsService.getCatFact()
                }
                val pics = async(Dispatchers.IO) {
                    picsService.getCatPicture()
                }
                try {
                    _state.postValue(Success(CatModel(facts.await(), pics.await())))
//                    throw SocketTimeoutException()
//                    throw IllegalArgumentException("TEST")
                } catch (ex: SocketTimeoutException) {
                    _state.postValue(Error(R.string.socket_error))
                } catch(ex: CancellationException){
                    throw ex
                } catch (ex: Throwable) {
                    _state.postValue(Error(R.string.general_error, ex.message))
                    throw ex
                }
            }
        }
    }

    fun start() {
        loadCats()
    }

    fun stop() {
        viewModelScope.coroutineContext.cancel()
    }
}