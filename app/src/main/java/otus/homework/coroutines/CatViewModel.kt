package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatViewModel(private val repository: Repository) : ViewModel() {

  private val _data = MutableLiveData<Result<Cat>>()
  val data: LiveData<Result<Cat>>
    get() = _data

  private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
    when (exception) {
      is SocketTimeoutException -> _data.value = Error("No response from server")
      else -> {
        _data.value = Error(exception.message ?: "something nasty happened")
        CrashMonitor.trackWarning(exception)
      }
    }
  }

  init {
    loadCat()
  }

  fun loadCat() {
    viewModelScope.launch(exceptionHandler) {
      val cat = async(Dispatchers.IO) {
        repository.getCat()
      }

      _data.value = Success(cat.await())
    }
  }
}