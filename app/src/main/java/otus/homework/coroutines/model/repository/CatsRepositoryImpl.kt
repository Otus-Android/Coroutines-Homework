package otus.homework.coroutines.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import otus.homework.coroutines.model.network.CatsApiService
import otus.homework.coroutines.model.network.CatsFactService
import otus.homework.coroutines.model.network.dto.FactDto
import otus.homework.coroutines.model.network.dto.ImageDto

class CatsRepositoryImpl(
    private val catsFactService: CatsFactService,
    private val catsApiService: CatsApiService
) : CatsRepository {

    override suspend fun getCatFact(): FactDto {
        return withContext(Dispatchers.IO) {
            catsFactService.getCatFact()
        }
    }

    override suspend fun getCatImage(): ImageDto {
        return withContext(Dispatchers.IO) {
            catsApiService.imageSearch().first()
        }
    }
}
