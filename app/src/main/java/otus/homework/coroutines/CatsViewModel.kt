package otus.homework.coroutines

import android.media.Image
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.models.ImageFact
import otus.homework.coroutines.models.PresentModel
import otus.homework.coroutines.services.ImageService
import otus.homework.coroutines.utils.ResultCats
import retrofit2.Response
import java.lang.Exception

class CatsViewModel(private val catsService: CatsService,
                    private val imageService: ImageService
): ViewModel(){


    private var _catsView: ICatsView? = null

    private val exceptionHandler = SupervisorJob() + CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
        println(exception)
    }


    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            var fact: Fact? = null
            var image: ImageFact? = null

            when(val responseFact = RepositoryFacts.callApiFacts(catsService)){
                is ResultCats.Success -> fact = responseFact.value.body()
                is ResultCats.Error -> _catsView?.showToast("Ошибка responseFact: ${responseFact.exception}")
            }

            when(val responseImage = RepositoryImage.callApiFacts(imageService)){
                is ResultCats.Success -> image = responseImage.value.body()
                is ResultCats.Error -> _catsView?.showToast("Ошибка responseImage: ${responseImage.exception}")
            }

            if (image != null && fact != null ){
                val presentModel = PresentModel(image, fact)
                _catsView?.populate(presentModel)
            }


       }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView

    }

    fun detachView() {
        _catsView = null
    }
}

object RepositoryFacts {
    suspend fun callApiFacts(catsService: CatsService) : ResultCats<Response<Fact>>{
        return CustomNetworkCall.safeApiCall() {
            catsService.getCatFact()
        }
    }
}

object RepositoryImage {
    suspend fun callApiFacts(imageService: ImageService) : ResultCats<Response<ImageFact>>{
        return CustomNetworkCall.safeApiCall() {
            imageService.getCatImage()
        }
    }
}

object CustomNetworkCall {
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> T): ResultCats<T> {
        return withContext(dispatcher) {
            try {
                var result = apiCall.invoke()
                ResultCats.Success(result)
            } catch (throwable: Exception) {
                ResultCats.Error(throwable)
            }
        }
    }
}