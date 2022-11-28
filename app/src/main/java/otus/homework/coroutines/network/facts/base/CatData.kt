package otus.homework.coroutines.network.facts.base

import otus.homework.coroutines.network.facts.base.image.CatImageUrlFile

data class CatData(
    var fact: AbsCatFact,
    var imageUrl: CatImageUrlFile
)
