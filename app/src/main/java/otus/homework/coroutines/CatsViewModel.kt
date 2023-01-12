package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import java.net.SocketTimeoutException

class CatsViewModel(
    private val context: Context,
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
): ViewModel() {
    fun loadCatData() = liveData(viewModelScope.coroutineContext + CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
    }) {
        supervisorScope {
            val factDeffered = async {
                catsService.getCatFact()
            }
            val imageDeffered = async {
                catsImageService.getCatImage()
            }
            try {
                val factResponse = factDeffered.await()
                val imageResponse = imageDeffered.await()
                if (
                    factResponse.isSuccessful && factResponse.body() != null
                    && imageResponse.isSuccessful && imageResponse.body() != null
                ) {
                    emit(CatViewData(
                        factResponse.body()!!.fact,
                        imageResponse.body()!!.file
                    ))
                }
            } catch (e: SocketTimeoutException) {
                Toast.makeText(context, R.string.service_response_error, Toast.LENGTH_SHORT).show()
            } catch (e: Throwable) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                CrashMonitor.trackWarning()
            }
        }
    }
}