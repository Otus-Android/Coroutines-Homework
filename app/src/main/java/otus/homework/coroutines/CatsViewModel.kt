package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatFactsService
import otus.homework.coroutines.api.CatImagesService
import otus.homework.coroutines.dto.CatData
import java.net.SocketTimeoutException

class CatsViewModel(
  private val catFactsService: CatFactsService,
  private val catImagesService: CatImagesService,
) : ViewModel() {

  private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    CrashMonitor.trackWarning(throwable)
    when (throwable) {
      is CancellationException -> {
        throw throwable
      }
      is SocketTimeoutException -> {
        _catLiveData.value = Error("Не удалось получить ответ от сервера")
      }
      else -> {
        _catLiveData.value = Error("${throwable.message}")
      }
    }
  }

  private val _catLiveData: MutableLiveData<Result<CatData>> = MutableLiveData()
  val catLiveData: LiveData<Result<CatData>> = _catLiveData

  init {
    getCatData()
  }

  fun getCatData() {
    viewModelScope.launch(coroutineExceptionHandler) {
      val catFact = async {
        catFactsService.getCatFact()
      }
      val catImageUri = async {
        catImagesService.getCatImageUri().catImageUri
      }
      _catLiveData.value = Success(CatData(catFact.await().text, catImageUri.await()))
    }
  }
}

class CatsViewModelFactory(
  private val catFactsService: CatFactsService,
  private val catImagesService: CatImagesService
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return CatsViewModel(catFactsService, catImagesService) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}