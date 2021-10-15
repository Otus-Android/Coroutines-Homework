package otus.homework.coroutines

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

interface CatsViewModel {
    val screenState: LiveData<Result<CatUiModel, *>>

    fun onInitComplete()
}

class CatsViewModelImpl(
    private val catsService: CatsService,
    private val catsServicePicture: CatsServicePicture
): CatsViewModel, ViewModel() {

    override val screenState = MutableLiveData<Result<CatUiModel, *>>()

    private var loadFactsJob: Job? = null

    val mainExceptionHandler = CoroutineExceptionHandler { _, t ->
        if (t is SocketTimeoutException) {
            screenState.postValue(Result.Error(R.string.cat_fact_server_error))
        } else {
            CrashMonitor.trackWarning()
            t.message?.let {
                screenState.postValue(Result.Error(it))
            }
        }
    }

    override fun onInitComplete() {
        loadFactsJob?.cancel()
        loadFactsJob = viewModelScope.launch(mainExceptionHandler) {
            val catFact = async { loadCatFact() }
            val catPicture = async { loadCatPicture() }
            populate(catFact.await(), catPicture.await())
        }
    }

    private fun populate(catFact: Fact?, catPicture: CatPictureUrl?) {
        if (catFact != null && catPicture != null)
            screenState.value = Result.Success(CatUiModel(catFact.text, catPicture.fileUrl))
    }

    private suspend fun loadCatPicture() =
        catsServicePicture.getCatPicture()

    private suspend fun loadCatFact() =
        catsService.getCatFact()

}

@SuppressLint("UNCHECKED_CAST")
class CatsViewModelFactory(
    private val catsService: CatsService,
    private val catsServicePicture: CatsServicePicture
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModelImpl(
            catsService,
            catsServicePicture
        ) as T
    }
}