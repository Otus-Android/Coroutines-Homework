package otus.homework.coroutines.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsRepository
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.models.CatFact
import otus.homework.coroutines.models.CatFactPic
import otus.homework.coroutines.Result
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsViewModel constructor(
    private val catsRepository: CatsRepository
) : ViewModel() {

    val catFactLiveData = MutableLiveData<Result>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    suspend fun onInitComplete() {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch(CoroutineName("CatsCoroutine")) {
                var result: Response<CatFact>
                lateinit var catFact: String
                lateinit var catPicBitmap: Bitmap

                val jobCatFact = launch {
                    try {
                        result = catsRepository.getCatFact()
                        if (result.isSuccessful && result.body() != null) {
                            catFact = result.body()!!.fact
                        } else {
                            onError("Error : ${result.message()} ")
                        }
                    } catch (socketTimeoutException: SocketTimeoutException) {
                        onError("Не удалось получить ответ от серверов")
                    } catch (e: Exception) {
                        onError("Exception message $e")
                    }
                }

                val jobCatPic = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                    try {
                        catPicBitmap = Picasso.get().load(catsRepository.getCatPic().body()?.get(0)?.url).get()
                    } catch (socketTimeoutException: SocketTimeoutException) {
                        onError("Не удалось получить ответ от серверов")
                    }
                }

                joinAll(jobCatFact, jobCatPic)

                catFactLiveData.postValue(Result.Success(CatFactPic(catFact, catPicBitmap)))
            }
        }
    }

    private fun onError(errorMessage: String) {
        catFactLiveData.value = Result.Error(errorMessage)
    }

}