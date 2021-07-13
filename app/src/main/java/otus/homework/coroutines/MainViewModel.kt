package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.entity.Animal
import otus.homework.coroutines.entity.Result
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageService
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    init {
        initState()
    }

    fun getContent() = initState()

    private fun initState() {
        viewModelScope.launch {
            try {
                val picture = imageService.getCatImage()
                val fact = catsService.getCatFact()

                _result.value = Result.Success(
                    Animal(
                        text = fact.text,
                        images = picture.file
                    )
                )
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _result.value = Result.Error(
                            errorState = ErrorState.SocketError
                        )
                    }
                    else -> {
                        _result.value = Result.Error(
                            errorState = ErrorState.OtherError(
                                e.message
                            )
                        )
                        CrashMonitor.trackWarning()
                    }
                }
            }
        }
    }
}