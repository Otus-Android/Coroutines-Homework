package otus.homework.coroutines.presentation.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.data.fact.CatsFactService
import otus.homework.coroutines.data.img.CatsImgService
import otus.homework.coroutines.presentation.CatsDataUI
import otus.homework.coroutines.utils.ErrorDisplay
import otus.homework.coroutines.utils.ManagerResources
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsFactService: CatsFactService,
    private val catsImgService: CatsImgService,
    private val errorDisplay: ErrorDisplay,
    private val exceptionHandler: CoroutineExceptionHandler,
    private val managerResources: ManagerResources,
    private val catsDataUiMapper: CatsDataUiMapper = CatsDataUiMapper()
) : ViewModel() {

    private val _viewStateLiveData = MutableLiveData<Result>()
    val viewStateLiveData: LiveData<Result> = _viewStateLiveData

    fun getNewCatsData() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val fact = async { catsFactService.getCatFact() }
                val img = async { catsImgService.getCatImg() }
                _viewStateLiveData.value = Result.Success(
                    CatsDataUI(img.await().imageUrl, fact.await().text),
                    catsDataUiMapper
                )
            } catch (exception: SocketTimeoutException) {
                _viewStateLiveData.value = Result.Error(managerResources.getString(R.string.server_error), errorDisplay)
            } catch (exception: Exception) {
                _viewStateLiveData.value = Result.Error(
                    exception.message ?: managerResources.getString(R.string.default_error),
                    errorDisplay
                )
                throw exception
            }
        }
    }
}