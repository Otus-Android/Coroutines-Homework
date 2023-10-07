package otus.homework.coroutines.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.presentation.CatContent
import otus.homework.coroutines.presentation.FactsRepository
import otus.homework.coroutines.presentation.PictureRepository
import otus.homework.coroutines.presentation.Result
import java.net.SocketTimeoutException

class CatsViewModel(
    private val factsRepository: FactsRepository,
    private val pictureRepository: PictureRepository
) : ViewModel() {

    private var _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> get() = _screenState

    private val handler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        val message = when (throwable) {
            is SocketTimeoutException -> TIME_OUT_EXCEPTION_MESSAGE
            else -> throwable.message ?: "error"
        }
        _screenState.postValue(ScreenState.Error(message))
    }

    init {
        getFactsByCoroutines()
    }

    fun getFactsByCoroutines() {
        viewModelScope.launch(Dispatchers.IO + CoroutineName(CATS_COROUTINE_NAME) + handler) {
            val jFact = async {
                factsRepository.getFact()
            }
            val jPic = async {
                pictureRepository.getImage()
            }
           val fact = when(val factResponse = jFact.await()){
                is Result.Success-> factResponse.data!!
                is Result.Error -> throw factResponse.throwable!!
            }

           val url = when(val picResponse = jPic.await()){
                is Result.Success->picResponse.data!!
                is Result.Error->throw picResponse.throwable!!
            }
            val content = CatContent(
               fact =  fact,
               image = url
            )
            _screenState.postValue(ScreenState.ShowContent(content))
        }
    }

    companion object {
        private const val TIME_OUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервером"
        private const val CATS_COROUTINE_NAME = "CatsCoroutine"
        private const val TAG = "CatsViewModel"

        fun getViewModelFactory(
            factsRepository: FactsRepository,
            pictureRepository: PictureRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CatsViewModel(
                        factsRepository, pictureRepository
                    ) as T
                }
            }
    }

}