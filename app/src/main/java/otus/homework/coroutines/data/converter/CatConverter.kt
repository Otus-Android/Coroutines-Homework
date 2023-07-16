package otus.homework.coroutines.data.converter

import otus.homework.coroutines.models.data.Fact
import otus.homework.coroutines.models.data.Image
import otus.homework.coroutines.models.domain.Cat

/**
 * Конвертер данных из [Fact] и списка [Image] в данные с информацией о кошке [Cat]
 */
class CatConverter {

    /** Сконвертировать факт [Fact] и список изобрежений [Image] в информацию о кошке [Cat] */
    fun convert(fact: Fact, images: List<Image>) =
        Cat(fact.fact, images.firstOrNull()?.url)
}