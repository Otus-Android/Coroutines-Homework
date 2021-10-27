package otus.homework.coroutines

import java.lang.Exception

class FetchFactAndImageUseCase(
    private val catsService: CatsService,
    private val randomImageService: RandomImageService
) {

    suspend operator fun invoke() : Result<Pair<Fact, RandomImage>> {
        return try {
            val fact = catsService.getCatFact()
            val randomImage = randomImageService.getRandomImage()
            Result.Success(fact to randomImage)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}