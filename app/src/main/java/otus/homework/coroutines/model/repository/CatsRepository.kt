package otus.homework.coroutines.model.repository

import otus.homework.coroutines.model.network.dto.FactDto
import otus.homework.coroutines.model.network.dto.ImageDto

interface CatsRepository {

    suspend fun getCatFact(): FactDto

    suspend fun getCatImage(): ImageDto
}