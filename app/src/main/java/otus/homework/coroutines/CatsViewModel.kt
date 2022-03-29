package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    val catData: LiveData<Result> get() = _catData
    private val _catData = MutableLiveData<Result>()

    fun requestCats() {

        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _catData.postValue(Error(throwable))
            CrashMonitor.trackWarning()
        }) {
            val cats = async { catsService.getCatFact() }
            val picture = async { catsService.getCatPicture() }

            coroutineScope {
                try {
                    _catData.postValue(Success(CatModel(cats.await(), picture.await())))
                } catch (e: SocketTimeoutException) {
                    _catData.postValue(Error(e))
                }
            }
        }
    }
}
