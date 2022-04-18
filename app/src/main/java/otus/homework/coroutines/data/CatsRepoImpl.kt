package otus.homework.coroutines.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatsRepoImpl(
    private val catsFactsService: CatsFactsService,
    private val catsPhotosService: CatsPhotosService
) : CatsRepo {

    override suspend fun getCatsFactsWithPhoto(): CatDto = withContext(Dispatchers.IO) {
        val catPhoto = catsPhotosService.getCatPhoto().photoUrl
        val catFact = catsFactsService.getCatFact()

        CatDto(fact = catFact, photoUrl = catPhoto)
    }
}