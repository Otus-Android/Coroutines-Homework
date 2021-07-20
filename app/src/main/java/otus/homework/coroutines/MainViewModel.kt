package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.entity.Animal
import otus.homework.coroutines.entity.Result
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageService

class MainViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            _result.value = Result.Error(exception)
            CrashMonitor.trackWarning()
        }

    fun initState() {
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val picture = imageService.getCatImage()
                val fact = catsService.getCatFact()
                _result.value = Result.Success(
                    Animal(
                        text = fact.text,
                        images = picture.file
                    )
                )
            }
        }
    }
}