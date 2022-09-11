package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsVm(
    private val catsService: CatsService,
    private val catPictureService: CatPictureService,
) : ViewModel() {

    init {
        onInitComplete()
    }

    private val _showToast: MutableLiveData<String> = MutableLiveData("")
    val showToast: LiveData<String> = _showToast
    fun resetToast() {
        _showToast.postValue("")
    }

    private val _catsData: MutableLiveData<CatsModel?> = MutableLiveData()
    val catsData: LiveData<CatsModel?> = _catsData


    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            //Обрабатываем непредвиденную ошибку
            CrashMonitor.trackWarning(throwable)
            changeUI(Error(throwable.message ?: throwable.toString()))
        }
        ) {
            lateinit var result: Result

            try {
                //Запускаем запросы одновременно
                val deferredFact = async { catsService.getCatFact() }
                val deferredPicture = async { catPictureService.getCatPicture() }

                //Ждём выполнение всех запросов
                val fact = deferredFact.await()
                val picture = deferredPicture.await()

                Log.e("CatsVm", "requests success!!!")
                result = Success(CatsModel(fact.text, picture.url))

            }
            //Обрабатываем определённые ошибки
            catch (e: SocketTimeoutException) {
                Log.e("CatsVm", "Error: $e")
                result = Error("Не удалось получить ответ от сервера")
            }

            changeUI(result)
        }
    }

    private fun changeUI(result: Result) {
        when (result) {
            is Error -> {
                _showToast.postValue(result.message)
            }
            is Success<*> -> {
                when (result.data) {
                    is CatsModel -> _catsData.postValue(result.data)
                }
            }
        }
    }

    class CatsVmProviderFactory(private val diContainer: DiContainer): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CatsVm(diContainer.service, diContainer.servicePicture) as T
    }
}