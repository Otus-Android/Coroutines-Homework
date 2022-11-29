package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import okhttp3.internal.wait

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    var result :MutableLiveData<Result?> = MutableLiveData()

    class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
         @Suppress("UNCHECKED_CAST")
         override fun <T : ViewModel> create(modelClass: Class<T>): T {
             return CatsViewModel(catsService) as T
         }
    }


    fun updateData() {
        val handler = CoroutineExceptionHandler { _, exception ->
            val msg = exception.message ?: "unknown exception"
            CrashMonitor.trackWarning(msg)
            result.value = Result.Error(msg)
        }
        viewModelScope.launch(Dispatchers.Main + CoroutineName("CatsCoroutine") + handler) {
            try {
                val fact = async {
                    catsService.getCatFact()
                }
                val img = async {
                    catsService.getCatImage()
                }

                listOf(fact, img).awaitAll()
                result.value = Result.Success(fact.getCompleted())
                result.value = Result.Success(img.getCompleted())
            } catch (e: java.net.SocketTimeoutException) {
                result.value = Result.Error("Не удалось получить ответ от сервером")
            }
        }
    }
}