package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CoroutinesApplication
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatsIconService
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

    private val toastTextMutableLiveData: MutableLiveData<Text> = MutableLiveData()
    private val catInfoMutableLiveData: MutableLiveData<CatInfoModel> = MutableLiveData()

    val toastTextLiveData: LiveData<Text> = toastTextMutableLiveData
    val catInfoLiveData: MutableLiveData<CatInfoModel> = catInfoMutableLiveData

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable: Throwable ->
            if (throwable is SocketTimeoutException) {
                onInitLoaded(CatResult.Error.ByRes(R.string.toast_exception))
            } else {
                onInitLoaded(CatResult.Error.ByString(throwable.message.toString()))
            }
        }

    fun onInit() {
        viewModelScope.launch(exceptionHandler) {

            val fact: Deferred<CatFact> = async(Dispatchers.IO) { catsService.getCatFact() }
            val iconDeferred: Deferred<CatIcon> = async(Dispatchers.IO) { iconService.getIcons().first() }
            val icon = iconDeferred.await()
            val model = CatInfoModel(fact.await().text, icon.url, icon.width, icon.height)
            onInitLoaded(CatResult.Success(model))
        }
    }

    private fun onInitLoaded(result: CatResult) {
        when (result) {
            is CatResult.Error -> {
                onClickFailed(result)
            }
            is CatResult.Success -> {
                catInfoMutableLiveData.postValue(result.catInfo)
            }
        }
    }

    private fun onClickFailed(result: CatResult.Error) {
        when (result) {
            is CatResult.Error.ByRes -> {
                toastTextMutableLiveData.postValue(Text.TextByRes(result.stringRes))
            }
            is CatResult.Error.ByString -> {
                CrashMonitor.trackWarning(result.message)
                toastTextMutableLiveData.postValue(Text.TextByString(result.message))
            }
        }
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