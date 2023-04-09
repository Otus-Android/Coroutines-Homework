package otus.homework.coroutines

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

    var catFact = MutableLiveData<Result<CatFact>>()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning(throwable)
            catFact.value = if (throwable is SocketTimeoutException) {
                Result.Error("Не удалось получить ответ от сервером")
            } else {
                Result.Error(throwable.message)
            }
        }

    fun getData() {

        viewModelScope.launch(coroutineExceptionHandler) {
            val desc = async { catsService.getCatFact() }.await()
            val pic = async { picsService.getPics() }.await()

            catFact.value = Result.Success(
                CatFact(
                    factText = desc.text,
                    imageUrl = pic.first().url
                )
            )
        }
    }

}