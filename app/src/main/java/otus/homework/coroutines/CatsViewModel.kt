package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsServiceImage: CatsService
) : ViewModel() {
    private var _catsResponse = MutableLiveData<Result<FactImage>>()
    val catsResponse: LiveData<Result<FactImage>>
        get() = _catsResponse

    fun getCatFactImage() {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning()
        }) {

                try {
                    val factResponseDeferred = async { withContext(Dispatchers.IO) {catsService.getCatFact() }}
                    val imageResponseDeferred = async { withContext(Dispatchers.IO) {catsServiceImage.getCatImage() }}

                    val factResponse = factResponseDeferred.await()
                    val imageResponse = imageResponseDeferred.await()

                    /**
                     * antonkazakov: ... Используй async чтобы распараллелить
                     * Вопрос: изначально я сделал без async чтобы они выполнялись полседовательно
                     * чтобы два результата использовать при создании объекта
                     * FactImage(factResponse, imageResponse)
                     * теперь при использовании async как это будет работать?
                     * допустим получаем результат factResponse, imageResponse - неодновременно
                     * FactImage(factResponse, imageResponse) будет создан из того что пришло первым,
                     * а второй параметр "уйдет" пустым
                     * или
                     * родительская корутину (которая запускает launch)
                     * "дождется" всех и только потом создаст FactImage(factResponse, imageResponse) ?
                     */
                    val factImage = FactImage(factResponse, imageResponse)
                    _catsResponse.value = Result.Success(factImage)

                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> {
                            _catsResponse.value =
                                Result.Error("Не удалось получить ответ от сервером", e)
                        }
                        else -> {
                            _catsResponse.value = Result.Error(e.message.toString(), e)
                            e.printStackTrace()
                        }
                    }
                }
        }
    }
}

sealed class Result<out T> {
    data class Success<out R>(val value: R) : Result<R>()
    data class Error(
        val message: String,
        val throwable: Throwable?
    ) : Result<Nothing>()
}