package otus.homework.coroutines.data.repository

import otus.homework.coroutines.data.network.CatsFactService
import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.domain.ICatRepository
import otus.homework.coroutines.domain.INetworkExceptionHandler
import otus.homework.coroutines.presenation.model.CatsCard
import java.lang.Exception
import java.util.concurrent.CancellationException

class CatRepository(
	val catsFactService: CatsFactService,
	val catsImageService: CatsImageService
) : ICatRepository {

	override suspend fun getCatCard(): CatsCard {
		val fact = catsFactService.getCatFact()
		val image = catsImageService.getImage()
		return CatsCard(fact.text, image.url)
	}
}