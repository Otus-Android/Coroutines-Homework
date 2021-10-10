package otus.homework.coroutines.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import otus.homework.coroutines.BuildConfig
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.domain.CatRandomFact
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.domain.Result
import java.net.SocketTimeoutException

class CatsRepositoryImpl(
    private val catsService: CatsService
) : CatsRepository {
    override suspend fun getCatRandomFact(): Result<CatRandomFact> {
        return try {
            coroutineScope {
                val factResponse =
                    async { catsService.getCatFact(BuildConfig.CAT_FACT_API_URL.plus(BuildConfig.CAT_FACT_API_URL_PATH)) }
                val imageResponse = async { catsService.getCatImage(BuildConfig.CAT_IMAGE_API_URL) }
                with(factResponse.await() to imageResponse.await()) {
                    if (first.isSuccessful && second.isSuccessful) {
                        Result.Success(convertToCatRandomFact(first.body(), second.body()))
                    } else {
                        val message = "${first.message()}${second.message()}"
                        CrashMonitor.trackWarning(message)
                        Result.Error(message)
                    }
                }
            }
        } catch (e: Exception) {
            CrashMonitor.trackWarning(if (e is SocketTimeoutException) "Не удалось получить ответ от сервера" else e.message.orEmpty())
            Result.Error(e.message.orEmpty())
        }
    }
}