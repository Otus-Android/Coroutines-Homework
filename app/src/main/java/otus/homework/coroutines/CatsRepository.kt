package otus.homework.coroutines

class CatsRepository(
    private val factService: CatsFactService,
    private val imageService: CatsImageLinkService
) {
    suspend fun getCatFact(): String =
        factService.getCatFact().text

    suspend fun getImageLink(): String =
        imageService.getImageLink()
}