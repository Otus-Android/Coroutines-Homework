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
import java.net.SocketTimeoutException


class CatsViewModel(
    private val factsRepository: FactsRepository,
    private val pictureRepository: PictureRepository
) : ViewModel() {

    private var _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> get() = _screenState


    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
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
            val catContent = CatContent(
                fact = jFact.await(),
                image = jPic.await()
            )
            _screenState.postValue(ScreenState.ShowContent(catContent))

        }
    }


    companion object {
        private const val TIME_OUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервером"
        private const val CATS_COROUTINE_NAME = "CatsCoroutine"
        private const val TAG = "TAG"


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