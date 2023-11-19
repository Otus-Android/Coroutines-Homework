package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CoroutinesApplication
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatsIconService
import otus.homework.coroutines.domain.CatsIconService.Companion.DEFAULT_ICON
import otus.homework.coroutines.domain.CatsService
import otus.homework.coroutines.domain.CrashMonitor
import otus.homework.coroutines.models.domain.CatFact
import otus.homework.coroutines.models.domain.CatIcon
import otus.homework.coroutines.models.presentation.CatInfoModel
import otus.homework.coroutines.models.presentation.Text
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: CatsService,
    private val iconService: CatsIconService
) : ViewModel() {

    private val catInfoMutableLiveData: MutableLiveData<CatResult> = MutableLiveData()
    val catResultLiveData: LiveData<CatResult> = catInfoMutableLiveData

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable: Throwable ->
        CrashMonitor.trackWarning(throwable.message.orEmpty())
    }

    fun onInit() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val fact: Deferred<CatFact> = async(Dispatchers.IO) { catsService.getCatFact() }
                val iconDeferred: Deferred<CatIcon> = async(Dispatchers.IO) { iconService.getIcons().first() }

                val icon = runCatching { iconDeferred.await() }.getOrDefault(DEFAULT_ICON)
                val model = CatInfoModel(fact.await().text, icon.url, icon.width, icon.height)
                onInitLoaded(CatResult.Success(model))
            } catch (ex: Exception) {
                handleError(ex)
            }

        }
    }

    private fun handleError(ex: Exception) {
        when (ex) {
            is SocketTimeoutException -> {
                onInitLoaded(CatResult.Error(Text.TextByRes(R.string.toast_exception)))
            }
            is CancellationException -> {
                throw ex
            }
            else -> {
                onInitLoaded(CatResult.Error(Text.TextByString(ex.message.orEmpty())))
            }
        }
    }

    private fun onInitLoaded(result: CatResult) {
        catInfoMutableLiveData.value = result
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val diContainer = (extras[APPLICATION_KEY] as CoroutinesApplication).diContainer
                return MainViewModel(diContainer.catsService, diContainer.catsIconService) as T
            }
        }
    }
}