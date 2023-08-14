package otus.homework.coroutines

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.model.CatModel
import java.net.SocketTimeoutException


class CatViewModel(
    private val factService: CatsFactService,
    private val imageService: CatsImageService
) : ViewModel() {
    private val _catModel: MutableLiveData<Result> = MutableLiveData<Result>()
    val catModel: LiveData<Result> = _catModel

    fun onInitComplete() {
        loadData()
    }

    private val handler = CoroutineExceptionHandler { _, throwable ->
        val message = throwable.message.toString()
        CrashMonitor.trackWarning(message)
    }

    private fun loadData() {
        viewModelScope.launch(handler) {
            try {
                val catFactJob = async { factService.getCatFact() }
                val catImageJob = async { imageService.getCatImage() }

                _catModel.value = Result.Success(
                    CatModel(
                        catFactJob.await().fact,
                        CatsImageService.BASE_URL + catImageJob.await().url
                    )
                )
            } catch (exception: SocketTimeoutException) {
                _catModel.value = Result.Error(
                    Throwable(
                        Resources.getSystem().getString(R.string.error_connection)
                    )
                )
            } catch (exception: CancellationException) {
                throw exception
            }
        }
    }
}
