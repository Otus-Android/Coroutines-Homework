package otus.homework.coroutines.data.converter

import otus.homework.coroutines.data.models.Fact
import otus.homework.coroutines.data.models.Image
import otus.homework.coroutines.domain.models.Cat

/**
 * Конвертер данных из [Fact] и списка [Image] в данные с информацией о кошке [Cat]
 */
class CatConverter {

    /** Сконвертировать факт [Fact] и список изобрежений [Image] в информацию о кошке [Cat] */
    fun convert(fact: Fact, images: List<Image>) =
        Cat(fact.fact, images.firstOrNull()?.url)
}