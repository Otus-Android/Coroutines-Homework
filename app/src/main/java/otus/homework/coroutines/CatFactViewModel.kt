package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

private const val TIME_OUT_ERROR_TOAST_MSG = "Не удалось получить ответ от сервером"
private const val UNKNOW_ERROR_TOAST_MSG = "НЕИЗВЕСТНАЯ ОШИБКА"

class CatFactViewModel : ViewModel() {

    private val diContainer = DiContainer()

    private val _catFact = MutableLiveData<CatFact>()
    val catFact: LiveData<CatFact> = _catFact

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun onInitComplete()  {
        viewModelScope.launch {
            val fact = async {
                if (getCatFact().success != null) {
                    getCatFact().success
                } else {
                    showErrorToast(getCatFact().mistake!!)
                    null
                }

            }
            val catImage = async(Dispatchers.IO) {
                val result = getImageUrl()
                if (result.success != null) {
                    Picasso.get().load(result.success).get()
                } else {
                    showErrorToast(getCatFact().mistake!!)
                    null
                }
            }
            val factResult = fact.await()
            val imageResult = catImage.await()
            if (factResult != null && imageResult != null) {
                _catFact.value = CatFact(imageResult, factResult)
            }
        }
    }

    private suspend fun getImageUrl(): Result<String, String> {
        return try {
            val image = diContainer.imageService.getCatImageUrl().urlImage
            Result.Success(image)
        } catch (e: Exception) {
            Result.Error(getErrorMessage(e))
        }
    }

    private suspend fun getCatFact(): Result<Fact?, String?> {
        return try {
            Result.Success(diContainer.service.getCatFact())
        } catch (e: Exception) {
            Result.Error(getErrorMessage(e))
        }
    }

    private fun getErrorMessage(e: Exception): String {
        return when (e) {
            is CancellationException -> throw e
            is SocketTimeoutException -> TIME_OUT_ERROR_TOAST_MSG
            else -> {
                CrashMonitor.trackWarning()
                e.message ?: UNKNOW_ERROR_TOAST_MSG
            }
        }
    }

    private fun showErrorToast(message: String) {
        _error.value = message
    }
}