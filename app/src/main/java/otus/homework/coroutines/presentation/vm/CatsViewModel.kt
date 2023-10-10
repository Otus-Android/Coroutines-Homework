package otus.homework.coroutines.presentation.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.data.CatFactsService
import otus.homework.coroutines.data.CatImagesService
import otus.homework.coroutines.domain.CrashMonitor
import otus.homework.coroutines.models.Cat
import otus.homework.coroutines.models.Result

class CatsViewModel(
    private val CatFactsService: CatFactsService,
    private val CatImagesService: CatImagesService
) : ViewModel() {

    private val _cat = MutableLiveData<Result<Cat>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning("${e.message}")
        _cat.value = Result.Error("${e.message}")
    }
    val cat: LiveData<Result<Cat>>
        get() = _cat

    fun getCat() {

        viewModelScope.launch(exceptionHandler) {
                val factJob = async(Dispatchers.IO) {
                    CatFactsService.getCatFact()
                }
                val imageJob = async(Dispatchers.IO) {
                    CatImagesService.getCatImage()[0]
                }
                val catResponse = Cat(factJob.await().fact, imageJob.await().url)
                Log.d("TAG", "Fact is ${catResponse.fact}, image is ${catResponse.imageUrl}")
                _cat.value = Result.Success(catResponse)
        }
    }

    fun cancelCoroutine() {
        viewModelScope.cancel()   // cancel job + all children
    }
}

class CatsViewModelFactory(
    private val CatFactsService: CatFactsService,
    private val CatImagesService: CatImagesService
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(CatFactsService, CatImagesService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
