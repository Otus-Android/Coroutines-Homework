package otus.homework.coroutines.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageService
import otus.homework.coroutines.ui.model.CatsResult
import otus.homework.coroutines.ui.model.CatsResult.Failure
import otus.homework.coroutines.ui.model.CatsResult.Loading
import otus.homework.coroutines.ui.model.CatsResult.Success
import otus.homework.coroutines.ui.model.FactVO
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.DispatchersProvider
import java.net.SocketTimeoutException

class CatsViewModel(
  private val catsService: CatsService,
  private val imageService: ImageService,
) : ViewModel() {
  companion object {
    private const val TAG = "CatsPresenter"
  }

  private val catsCoroutineExceptionHandler = CoroutineExceptionHandler { _, th ->
    CrashMonitor.trackWarning(TAG, th)
    resultLiveData.value = Failure(th, "General error!")
  }

  val resultLiveData = MutableLiveData<CatsResult>()

  init { load() }

  fun load() = viewModelScope.launch(catsCoroutineExceptionHandler) {
    resultLiveData.value = Loading

    try {
      val factJob = async(DispatchersProvider.io()) { catsService.getCatFact() }
      val fact = factJob.await()

      val imageJob = async(DispatchersProvider.io()) { imageService.getCatImage() }
      val image = imageJob.await()

      val factViewObject = FactVO(fact.fact, image.file)

      resultLiveData.value = Success(factViewObject)

      CrashMonitor.trackDebug(TAG, fact.toString())
      CrashMonitor.trackDebug(TAG, image.toString())
    } catch (ste: SocketTimeoutException) {
      resultLiveData.value = Failure(ste, "Something went wrong with network/server!")
    }
  }
}