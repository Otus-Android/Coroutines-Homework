package otus.homework.coroutines.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.ui.model.CatContent
import otus.homework.coroutines.presentation.FactsRepository
import otus.homework.coroutines.presentation.PictureRepository
import otus.homework.coroutines.presentation.Result
import otus.homework.coroutines.presentation.model.FactModel
import otus.homework.coroutines.presentation.model.PictureModel
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
        viewModelScope.launch(CoroutineName(CATS_COROUTINE_NAME) + handler) {
            val jFact = async {
                factsRepository.getFact()
            }
            val jPic = async {
                pictureRepository.getImage()
            }

            val fact =
                when (val factResponse = jFact.await()) {
                    is Result.Success<FactModel> -> factResponse.data
                    is Result.Error -> {
                        _screenState.value =
                            ScreenState.Error(
                                factResponse.throwable.message ?: "error"
                            )
                        FactModel.getDefault()
                    }
                }

            val picture =
                when (val picResponse = jPic.await()) {
                    is Result.Success<PictureModel> -> picResponse.data
                    is Result.Error -> {
                        _screenState.value =
                            ScreenState.Error(
                                picResponse.throwable.message ?: "error"
                            )
                        PictureModel.getDefault()
                    }
                }

            val content = CatContent(
                fact = fact.text,
                image = picture.url
            )
            if (content.image.isNotEmpty()) {
                _screenState.value = ScreenState.ShowContent(content)
            }
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