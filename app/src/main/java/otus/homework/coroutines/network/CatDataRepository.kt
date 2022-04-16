package otus.homework.coroutines.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import otus.homework.coroutines.network.dto.CatData
import otus.homework.coroutines.network.services.FactService
import otus.homework.coroutines.network.services.ImageService

class CatDataRepository(
    private val factService: FactService,
    private val imageService: ImageService
) {

    suspend fun request(): CatData = withContext(Dispatchers.IO) {
        val catFactResponse = async { factService.getCatFact() }
        val catImageResponse = async { imageService.getCatImage() }
        CatData(catFactResponse.await(), catImageResponse.await())
    }
}