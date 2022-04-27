package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsModel(
    ) : ViewModel()  {

    private val diContainer = DiContainer()
    private val diViewContainer = DiViewContainer()

    private val catsService = diContainer.service
    private val catsViewService = diViewContainer.service

    private var _state = MutableLiveData<Result<*>>()
    val state: LiveData<Result<*>>
        get() = _state

    //run async routing
    fun getFileViewAndText(){

        viewModelScope.launch {

            val handler = CoroutineExceptionHandler { _, exception ->
                CrashMonitor.trackWarning()
                _state.value = Error(exception.toString())
            }

            val text = async(handler) {
                 catsService.getCatFact().text
            }.await()

            val source = async(handler) {
                catsViewService.getCatView().file
            }.await()

            _state.postValue(Success<Fact>( FactShort(text = text, source = source )))
        }
    }
}