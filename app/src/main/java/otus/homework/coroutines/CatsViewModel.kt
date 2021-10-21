package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {
    private val _catsLiveData: MutableLiveData<Result<CatModel>> = MutableLiveData()
    val catsLiveData: LiveData<Result<CatModel>>
        get() = _catsLiveData

    private var _catsView: ICatsView? = null
    private val exHandler = CoroutineExceptionHandler { _, ex ->
        CrashMonitor.trackWarning(ex)
    }

    fun onViewInitializationComplete() {

        viewModelScope.launch(exHandler + SupervisorJob()) {
            val catFactDef = async(Dispatchers.IO) { catsService.getCatFact() }
            val catImgDef = async(Dispatchers.IO) { catsImageService.getCatImage() }
            try {
                _catsLiveData.postValue(
                    Result.Success(
                        CatModel(
                            catFactDef.await(),
                            catImgDef.await()
                        )
                    )
                )
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        _catsLiveData.postValue(
                            Result.Error(
                                "Не удалось получить ответ от сервера",
                                ex
                            )
                        )
                    }
                    is CancellationException -> {
                        throw ex
                    }
                    else -> {
                        _catsLiveData.postValue(Result.Error(ex.message.toString(), ex))
                    }
                }
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }

}