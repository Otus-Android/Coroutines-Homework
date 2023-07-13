package otus.homework.coroutines.data.converter

import otus.homework.coroutines.models.Fact
import otus.homework.coroutines.models.Image
import otus.homework.coroutines.models.Cat

class CatConverter {

    fun convert(fact: Fact, images: List<Image>) =
        Cat(fact.fact, images.firstOrNull()?.url)
}