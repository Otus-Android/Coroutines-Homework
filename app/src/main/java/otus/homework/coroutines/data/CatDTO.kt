package otus.homework.coroutines.data

import otus.homework.coroutines.models.CatPhoto
import otus.homework.coroutines.models.Fact

data class CatDTO(
    val photo: CatPhoto,
    val fact: Fact
)