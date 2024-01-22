package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import otus.homework.coroutines.api.services.facts.IFactsService
import otus.homework.coroutines.api.services.photos.IPhotoService
import otus.homework.coroutines.entitiy.CatFact
import otus.homework.coroutines.util.CrashMonitor
import otus.homework.coroutines.util.dangerCast
import otus.homework.coroutines.util.runCatching
import retrofit2.HttpException
import java.lang.IllegalStateException
import java.net.SocketTimeoutException

sealed interface CatsState {

    data class Success(val fact: CatFact) : CatsState
    data class Error(val error: Throwable) : CatsState
}

class CatsViewModel(
    private val factsService: IFactsService,
    private val photoService: IPhotoService,
    private val viewModelScope: CoroutineScope // implementation without ViewModel-ktx extensions
) : ViewModel() {


    suspend fun fetchCatFact(): CatsState = with(viewModelScope) {
        val catFact = runCatching {
            coroutineScope {
                val fact = async {
                        factsService.getCatFact().let {
                            it.body() ?: throw IllegalStateException(
                                "Successful response was captured with empty body",
                                HttpException(it)
                            )
                        }
                }

                val photo = async {
                        photoService.getRandomPhoto().let {
                            it.body() ?: throw IllegalStateException(
                                "Successful response was captured with empty body",
                                HttpException(it)
                            )
                        }
                }

                val awaitedData = awaitAll(fact, photo)

                val loadedFact = awaitedData.component1()
                    .dangerCast<Fact>()

                val loadedPhoto = awaitedData.component2()
                    .dangerCast<List<Photo>>()
                    .first()

                CatFact(
                    photoUri = loadedPhoto.url,
                    funFact = loadedFact.fact
                )
            }
        }.onFailure {
            if (it !is SocketTimeoutException) {
                CrashMonitor.trackWarning()
            }
        }

        return catFact.fold(
            onSuccess = {
                CatsState.Success(it)
            },
            onFailure = {
                CatsState.Error(it)
            }
        )
    }

    override fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}