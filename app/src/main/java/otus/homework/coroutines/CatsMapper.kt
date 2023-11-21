package otus.homework.coroutines

class CatsMapper {

    fun toFactAndImage(fact: String?, image: String?): FactAndImageModel = FactAndImageModel(
        fact = fact.orEmpty(),
        image = image.orEmpty()
    )
}