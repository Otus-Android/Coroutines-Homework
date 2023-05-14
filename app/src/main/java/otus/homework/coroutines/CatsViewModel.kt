package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val picsService: PicsService
) : ViewModel() {

    private var _catFact = MutableLiveData<Result<CatFact>>()
    var catFact: LiveData<Result<CatFact>> = _catFact

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning(throwable)
        }

    fun getData() {

        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val desc = async { catsService.getCatFact() }.await()
                val pic = async { picsService.getPics() }.await()

                _catFact.value = Result.Success(
                    CatFact(
                        factText = desc.text,
                        imageUrl = pic.first().url
                    )
                )
            } catch (e: Exception) {
                _catFact.value = if (e is SocketTimeoutException) {
                    Result.Error("Не удалось получить ответ от сервером")
                } else {
                    Result.Error(e.message)
                }
            }

        }
    }

}