package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import otus.homework.coroutines.data.CatData

class CatsRepository(
    private val factService: CatsFactService,
    private val imageService: CatsImageService
) {
    suspend fun getCatData(): CatData = withContext(Dispatchers.IO) {
        val fact = async { factService.getCatFact() }
        val image = async { imageService.getCatImage().first() }

        CatData(
            fact = fact.await(),
            image = image.await()
        )
    }
}