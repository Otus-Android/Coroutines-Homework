package otus.homework.coroutines.domain

import otus.homework.coroutines.data.entities.Fact
import otus.homework.coroutines.presenation.model.CatsCard

interface ICatRepository {
	suspend fun getCatCard(): CatsCard
}