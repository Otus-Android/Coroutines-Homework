package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatViewModel(private val catsService: CatsService):ViewModel() {


    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error


    private val _gen: MutableLiveData<General> = MutableLiveData()
    val gen: LiveData<General> = _gen
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable.localizedMessage)
        }
    fun getCat(){
        viewModelScope.launch(Dispatchers.Main + CoroutineName("CatsCoroutine")+coroutineExceptionHandler){
           val fact = async {
               val result = State.on {  catsService.getCatFact()}
               when (result) {
                   is State.error -> {
                      _error.value = result.exception.message.toString()
                   }
                   is State.success -> {
                       result.data
                   }
               }
            }.await()
           val img = async {
               val result = State.on { catsService.getCatFactImg().first()}
               when (result) {
                   is State.error ->  _error.value = result.exception.message.toString()
                   is State.success -> result.data
               }
            }.await()
            _gen.value = General(fact as Fact,img as Img)
        }
    }
}