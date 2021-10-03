package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * @author Dmitry Kartsev (Jag)
 * @sinse 20.06.2021
 */
class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _catsFactsFlow = MutableStateFlow<CatNewPresentationModel?>(null)
    private val _errorMessagesFlow = MutableStateFlow<Int?>(null)
    val catsFactsFlow: StateFlow<CatNewPresentationModel?> = _catsFactsFlow
    val errorMessagesFlow: StateFlow<Int?> = _errorMessagesFlow


    init {
        requestFact()
    }

    fun requestFact() = viewModelScope.launch {
        try {
            val imageResponse = async { catsService.getRandomCatImage() }
            val factResponse = async { catsService.getCatFact() }

            handleRemoteResponses(factResponse.await(), imageResponse.await())
        } catch (exception: SocketTimeoutException) {
            _errorMessagesFlow.value = R.string.connection_timeout
//            _catsView?.showMessage(R.string.connection_timeout)
        } catch (exception: Exception) {
            CrashMonitor.trackWarning(exception.message.orEmpty())
        }
    }

    private fun handleRemoteResponses(
        factResponse: Response<Fact>,
        imageResponse: Response<RandomCatImage>
    ) {
        if (factResponse.isSuccessful && factResponse.body() != null &&
            imageResponse.isSuccessful && imageResponse.body() != null
        ) {
            _catsFactsFlow.value = CatNewPresentationModel(
                checkNotNull(factResponse.body()).text,
                checkNotNull(imageResponse.body()).file
            )

        } else CrashMonitor.trackWarning(factResponse.message())
    }
}

data class CatNewPresentationModel(
    val factText: String,
    val imageSource: String
)

class CatsViewModelFactory(private val catsService: CatsService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CatsViewModel(catsService) as T
}