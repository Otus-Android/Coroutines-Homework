package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatViewModel(
    private val catsService: CatService,
    private val catsImageService: CatService
) : ViewModel() {

    private val catsLiveData = MutableLiveData<Result<FactAndImage>>()
    val getCatsLiveData: LiveData<Result<FactAndImage>>
        get() = catsLiveData

    fun onInitComplete() {

        viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }) {
            try {
                val factResponse =
                    withContext(Dispatchers.Default) { catsService.getCatFact() }
                val imageResponse =
                    withContext(Dispatchers.Default) { catsImageService.getCatImage() }

                catsLiveData.value =
                    Result.Success(FactAndImage(factResponse.text, imageResponse.file))

            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        catsLiveData.value = Result.Error(R.string.socket_timeout_message.toString())
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        catsLiveData.value = Result.Error(ex.message ?: "")
                    }
                }
            }
        }
    }

    class ViewModelFactory(
        private val catsTextService: CatService,
        private val catsImageService: CatService
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
                return CatViewModel(catsTextService, catsImageService) as T
            }
            throw IllegalArgumentException(R.string.error_create_viewmodel_message.toString())
        }
    }
}