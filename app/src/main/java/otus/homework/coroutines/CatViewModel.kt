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

    private val _gen: MutableLiveData<State<General>> = MutableLiveData()
    val gen: LiveData<State<General>> = _gen
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable.localizedMessage)
        }
    fun getCat(){
        viewModelScope.launch(coroutineExceptionHandler){
           val fact = async {
               catsService.getCatFact()

            }
           val img = async {
               catsService.getCatFactImg().first()
            }
            _gen.value = State.on { General(fact.await(),img.await())}
        }
    }
}