package otus.homework.coroutines.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsFactService
import otus.homework.coroutines.CatsPicService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.Result
import otus.homework.coroutines.models.CatFact
import otus.homework.coroutines.models.CatFactPic
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsViewModel constructor(
    private val serviceFact: CatsFactService,
    private val servicePic: CatsPicService
) : ViewModel() {

    var _catFactLiveData = MutableLiveData<Result>()
    val catFactLiveData: LiveData<Result>
        get() = _catFactLiveData
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    suspend fun onInitComplete() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler + CoroutineName("CatsCoroutine")) {
                var result: Response<CatFact>
                var catFact = ""
                lateinit var catPicBitmap: Bitmap

                launch(exceptionHandler) {
                    try {
                        result = serviceFact.getCatFact()
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
                }.join()

                launch(exceptionHandler) {
                    try {
                        catPicBitmap =
                            Picasso.get().load(servicePic.getCatPic().body()?.get(0)?.url).get()
                    } catch (socketTimeoutException: SocketTimeoutException) {
                        onError("Не удалось получить ответ от серверов")
                    }
                }.join()

                launch(Dispatchers.Main + exceptionHandler) {
                    println(this.coroutineContext)
                    _catFactLiveData.value = Result.Success(CatFactPic(catFact, catPicBitmap))
                }
            }
        }

        private fun onError(errorMessage: String) {
            _catFactLiveData.value = Result.Error(errorMessage)
        }

        public override fun onCleared() {
            _catFactLiveData = MutableLiveData<Result>()
            super.onCleared()
        }
    }