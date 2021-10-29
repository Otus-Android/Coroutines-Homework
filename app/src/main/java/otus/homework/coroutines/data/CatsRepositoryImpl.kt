package otus.homework.coroutines.data

import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import otus.homework.coroutines.BuildConfig
import otus.homework.coroutines.domain.CatRandomFact
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.domain.Result
import otus.homework.coroutines.utils.CoroutineDispatchers
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class CatsRepositoryImpl(
    private val catsService: CatsService,
    private val coroutineDispatchers: CoroutineDispatchers
) : CatsRepository {
    override suspend fun getCatRandomFact(): Result<CatRandomFact> {
        return withContext(coroutineDispatchers.ioDispatcher) {
            val factDeferred = async { getCatFact() }
            val imageDeferred = async { getCatImage() }
            with(factDeferred.await() to imageDeferred.await()) {
                if (first is Result.Success || second is Result.Success) {
                    return@with Result.Success(
                        CatRandomFact(
                            (first as? Result.Success)?.value,
                            (second as? Result.Success)?.value
                        )
                    )
                } else {
                    return@with Result.Error("${(first as? Result.Error)?.errorMessage} ${(second as? Result.Error)?.errorMessage}")
                }
            }
        }
    }

    private suspend fun getCatFact(): Result<String?> = processRequest(
        request = { catsService.getCatFact("$CAT_FACT_API_URL$CAT_FACT_API_URL_PATH") },
        onSuccess = { fact -> fact?.text }
    )

    private suspend fun getCatImage(): Result<String?> = processRequest(
        request = { catsService.getCatImage(CAT_IMAGE_API_URL) },
        onSuccess = { imageUrl -> imageUrl?.url }
    )

    @Throws(Exception::class)
    private suspend fun <T, R : Any> processRequest(
        request: suspend () -> Response<T>,
        onSuccess: (T?) -> R?
    ): Result<R?> {
        return try {
            val response = request.invoke()
            if (response.isSuccessful) {
                Result.Success(onSuccess.invoke(response.body()))
            } else {
                throw IOException("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            if (e is SocketTimeoutException) {
                Result.Error("Не удалось получить ответ от сервера")
            } else {
                throw e
            }
        }
    }

    private companion object {
        const val CAT_FACT_API_URL = "https://cat-fact.herokuapp.com/facts/"
        const val CAT_FACT_API_URL_PATH = "random?animal_type=cat"
        const val CAT_IMAGE_API_URL = "https://aws.random.cat/meow"
    }
}