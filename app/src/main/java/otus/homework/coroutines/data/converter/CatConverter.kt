package otus.homework.coroutines.data.converter

import otus.homework.coroutines.models.data.Fact
import otus.homework.coroutines.models.data.Image
import otus.homework.coroutines.models.domain.Cat

class CatConverter {

    fun convert(fact: Fact, images: List<Image>) =
        Cat(fact.fact, images.firstOrNull()?.url)
}