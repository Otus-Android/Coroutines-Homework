package otus.homework.coroutines

class FetchFactAndImageUseCase(
    private val catsService: CatsService,
    private val randomImageService: RandomImageService
) {

    suspend operator fun invoke() : Pair<Fact, RandomImage> {
        val fact = catsService.getCatFact()
        val randomImage = randomImageService.getRandomImage()
        return (fact to randomImage)
    }
}