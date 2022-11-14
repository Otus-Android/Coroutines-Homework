package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: CatsImageService
) : ViewModel() {

    private val catEntity = MutableLiveData<Result<CatEntity>>()

    lateinit var resourceProvider: ResourceProvider

    fun getCatEntity() = catEntity

    private val handler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e.message)

        catEntity.value =
            Result.Error(e.message ?: resourceProvider.getString(R.string.unknown_error))
    }

    init {
        getCatFact()
    }

    fun getCatFact() {
        viewModelScope.launch(handler) {
            val factAsync = async { catsService.getCatFact() }
            val imageAsync = async { imageService.getCatImage() }

            try {
                val factResult = factAsync.await()
                val imageResult = imageAsync.await()

                if (factResult.isSuccessful && factResult.body() != null &&
                    imageResult.isSuccessful && imageResult.body() != null
                ) {
                    val entity = CatEntity(factResult.body()!!, imageResult.body()!!)

                    catEntity.value = Result.Success(entity)
                } else {
                    catEntity.value =
                        Result.Error(resourceProvider.getString(R.string.unknown_error))
                }
            } catch (e: SocketTimeoutException) {
                catEntity.value = Result.Error(resourceProvider.getString(R.string.timeout_error))
            }
        }
    }
}

class CatsViewModelFactory constructor(
    private val catsService: CatsService,
    private val imageService: CatsImageService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catsService, imageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
