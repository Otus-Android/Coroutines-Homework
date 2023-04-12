package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainViewModel (
    private val factService: FactService,
    private val picService: PicService): ViewModel() {

    private var resultMutable = MutableLiveData<Result>()
    val result: LiveData<Result> = resultMutable

    fun getCatData(){
        viewModelScope.launch (handler){
                val fact = async { factService.getCatFact().fact }
                val picUrl = async { picService.getCatPic()[0].url }
                val catData = CatData(picUrl.await(), fact.await())
                resultMutable.value = Success(catData)
        }
    }

    private val handler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable.message.toString())
        if (throwable is SocketTimeoutException) {
            resultMutable.value = Error("Не удалось получить ответ от сервера", true)
        } else {
            resultMutable.value = Error(throwable.message.toString(), false)
        }
    }
}