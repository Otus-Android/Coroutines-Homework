package otus.homework.coroutines.presentation

import otus.homework.coroutines.network.CatImage
import otus.homework.coroutines.network.Fact

data class CatModel(
    val fact: Fact?,
    val image: CatImage?
)