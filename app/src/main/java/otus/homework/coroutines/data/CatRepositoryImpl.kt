package otus.homework.coroutines.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import otus.homework.coroutines.data.converter.CatConverter
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.models.Cat

class CatRepositoryImpl(
    private val factService: FactService,
    private val imageService: ImagesService,
    private val converter: CatConverter,
) : CatRepository {

    override suspend fun getCatInfo(): Cat = coroutineScope {
        val factDeferred = async { factService.getCatFact() }
        val imagesDeferred = async { imageService.getCatImages() }
        converter.convert(factDeferred.await(), imagesDeferred.await())
    }
}