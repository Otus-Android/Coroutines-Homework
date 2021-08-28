package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(private val factsService: ActivitiesService,
                    private val pictureService: PictureService) : ViewModel() {

    private val result = MutableLiveData<Result>()
    fun getResult(): LiveData<Result> = result

    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            result.value = when (exception) {
                is SocketTimeoutException -> Result.Error(messageRes = R.string.timeout_message)
                else -> {
                    CrashMonitor.trackWarning()
                    Result.Error(exception.message)
                }
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            val factDef = async(Dispatchers.IO) {
                factsService.getActivity()
            }
            val pictureDef = async(Dispatchers.IO) {
                pictureService.getCatPicture()
            }
            val factWithPicture = FactWithPicture(factDef.await().activity, pictureDef.await().pictureUrl)
            result.value = Result.Success(factWithPicture)
        }
    }
}