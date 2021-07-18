package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    private val result = MutableLiveData<Result>()
    fun getResult(): LiveData<Result> = result

    lateinit var diContainer: DiContainer
    private val factsService: CatsService by lazy { diContainer.factsService }
    private val pictureService: PictureService by lazy { diContainer.pictureService }

    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch(Dispatchers.Main) {
            when (exception) {
                is SocketTimeoutException -> result.value = Result.Error(messageRes = R.string.timeout_message)
                else -> {
                    Result.Error(exception.message)
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            val factDef = async(Dispatchers.IO) {
                val response = factsService.getCatFact()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!![0]
                } else {
                    throw Exception(response.errorBody().toString())
                }
            }
            val pictureDef = async(Dispatchers.IO) {
                val response = pictureService.getCatPicture()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!
                } else {
                    throw Exception(response.errorBody().toString())
                }
            }
            val factWithPicture = FactWithPicture(factDef.await().text, pictureDef.await().pictureUrl)
            result.value = Result.Success(factWithPicture)
        }
    }
}