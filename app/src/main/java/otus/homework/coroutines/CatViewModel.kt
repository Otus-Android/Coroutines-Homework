package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatViewModel(private val service: CatsService) : ViewModel() {

  private val _data = MutableLiveData<Result>()
  val data: LiveData<Result>
    get() = _data

  private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
      CrashMonitor.trackWarning(exception)
  }

  fun loadCat() {
    viewModelScope.launch(exceptionHandler) {
      supervisorScope {
        val catFact = async(Dispatchers.IO) { service.getCatFact() }
        val catImage = async(Dispatchers.IO) { service.getCatImage() }
        try {
          _data.value = Success(Cat(catFact.await().text, catImage.await().src))
        } catch (e: SocketTimeoutException) {
          _data.value = Error("No response from server")
        } catch (e: CancellationException) {
          throw e
        }
      }
    }
  }
}