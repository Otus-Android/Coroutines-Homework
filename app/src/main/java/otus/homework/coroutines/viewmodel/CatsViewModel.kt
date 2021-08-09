package otus.homework.coroutines.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatData
import otus.homework.coroutines.utils.DiContainer

class CatsViewModel(
    private val diContainer: DiContainer,
) : ViewModel() {

    init {
        onInitComplete()
    }

    private val _catDataResponse = MutableLiveData<Result<CatData>>()
    val catDataResponse: LiveData<Result<CatData>>
        get() = _catDataResponse

    private fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }) {
            val catFactResponseDef =
                async(Dispatchers.IO) { diContainer.catFactService.getCatFact() }
            val catImageResponseDef =
                async(Dispatchers.IO) { diContainer.catImageService.getCatImage() }

            val catFactResponse = catFactResponseDef.await()
            val catImageResponse = catImageResponseDef.await()

            val factImage = CatData(catFactResponse, catImageResponse)

            _catDataResponse.value = Result.Success(factImage)
        }
    }

    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val data: T) : Result<T>()
        data class Error(
            val exception: Exception,
            val error: String
        ) : Result<Nothing>()
    }

}