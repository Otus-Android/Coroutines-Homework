package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

  private val diContainer = DiContainer()

  private val _catsInfoStateLiveData = MutableLiveData<Result>()
  val catInfoState: LiveData<Result> = _catsInfoStateLiveData

  private val handler = CoroutineExceptionHandler { _, exception -> handleError(exception) }

  init {
      onInitComplete()
  }

  fun onInitComplete() {
    viewModelScope.launch(handler) {
      _catsInfoStateLiveData.value = Result.Success(fetchCatInfo())
    }
  }

  private suspend fun fetchCatInfo(): CatInfo {
    val factDeferred =
        viewModelScope.async { diContainer.service.getCatFact().body()?.fact.orEmpty() }
    val imageDeferred = viewModelScope.async { diContainer.image.getCatImage().body().orEmpty() }

    return CatInfo(factDeferred.await(), imageDeferred.await())
  }

  private fun handleError(t: Throwable) {
    when (t) {
      is SocketTimeoutException -> {
        _catsInfoStateLiveData.value = Result.Error("Не удалось получить ответ от сервера")
      }
      else -> {
        CrashMonitor.trackWarning()
        t.message?.let { message -> _catsInfoStateLiveData.value = Result.Error(message) }
      }
    }
  }
}
