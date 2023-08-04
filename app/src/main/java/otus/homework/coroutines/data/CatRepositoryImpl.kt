package otus.homework.coroutines.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import otus.homework.coroutines.data.converter.CatConverter
import otus.homework.coroutines.data.models.Fact
import otus.homework.coroutines.data.models.Image
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.domain.models.Cat

/**
 * Репозиторий информации о кошке [CatRepository]
 *
 * @param factService сервис получения случайного факта
 * @param imageService сервис получения случайных изображений
 * @param converter конвертер данных из [Fact] и списка [Image] в данные с информацией о кошке [Cat]
 */
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