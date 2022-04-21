package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.api.CatFactService
import otus.homework.coroutines.api.CatPhotoService
import otus.homework.coroutines.data.CatDTO
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catFactService: CatFactService,
    private val catPhotoService: CatPhotoService
) : ViewModel() {

    private val _catData = MutableStateFlow<Result<CatDTO>?>(null)
    val catData: StateFlow<Result<CatDTO>?> = _catData

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val downloadFactJob = async {
                catFactService.getCatFact()
            }

            val downloadPhotoJob = async {
                catPhotoService.getCatPhoto()
            }

            val catDTO = CatDTO(
                photo = downloadPhotoJob.await(),
                fact = downloadFactJob.await()
            )
            _catData.value = Result.Success(catDTO)
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is SocketTimeoutException) {
            _catData.value = Result.Error("Не удалось получить ответ от сервера")
        } else {
            throwable.message?.let { errorMessage ->
                _catData.value = Result.Error(errorMessage)
            }
            CrashMonitor.trackWarning()
        }
    }
}