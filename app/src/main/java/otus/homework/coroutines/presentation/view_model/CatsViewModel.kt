package otus.homework.coroutines.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.IResourceProvider
import otus.homework.coroutines.R
import otus.homework.coroutines.data.Cat
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.service.CatFactService
import otus.homework.coroutines.service.CatPictureService
import otus.homework.coroutines.service.CrashMonitor
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catFactService: CatFactService,
    private val catPictureService: CatPictureService,
    private val resources: IResourceProvider
) : ViewModel() {

    private val _state = MutableLiveData<Result<Cat>>()
    val state: LiveData<Result<Cat>> = _state

    private val handler = CoroutineExceptionHandler { _, _ ->
        CrashMonitor.trackWarning()
    }

    fun getCats() {
        viewModelScope.launch(handler) {
            val fact = async {
                catFactService.getCatFact()
            }
            val picture = async {
                catPictureService.getCatPicture()
            }

            try {
                _state.value = Result.Success(
                    Cat(
                        text = fact.await().text,
                        picture = picture.await().file
                    )
                )
            } catch (error: SocketTimeoutException) {
                _state.value = Result.Error(resources.getString(R.string.server_exception_message))
            } catch (error: Throwable) {
                _state.value = Result.Error(resources.getString(R.string.server_exception_message))
            }
        }
    }
}