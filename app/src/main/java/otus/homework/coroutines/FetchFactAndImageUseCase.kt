package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class FetchFactAndImageUseCase(
    private val catsService: CatsService,
    private val randomImageService: RandomImageService
) {

    suspend operator fun invoke() : Pair<Fact, RandomImage> = coroutineScope {
        val fact = async { catsService.getCatFact() }
        val randomImage = async { randomImageService.getRandomImage() }
        (fact.await() to randomImage.await())
    }
}