package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.net.SocketTimeoutException

class FetchFactAndImageResultUseCase(
    private val catsService: CatsService,
    private val randomImageService: RandomImageService
) {

    suspend operator fun invoke(): Result<Pair<Fact, RandomImage>> = coroutineScope {
        try {
            val fact = async { catsService.getCatFact() }
            val randomImage = async { randomImageService.getRandomImage() }
            Result.Success(fact.await() to randomImage.await())
        } catch (e: SocketTimeoutException) {
            Result.Error(e)
        }
    }
}