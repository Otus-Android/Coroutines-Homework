package otus.homework.coroutines.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import otus.homework.coroutines.model.CatInfo
import otus.homework.coroutines.repository.IFactRepository
import otus.homework.coroutines.repository.IPODRepository

class CatInfoUseCase(
    private val factRepository: IFactRepository,
    private val podRepository: IPODRepository
) {

    private companion object {
        const val API_KEY = "2zmSP2v2vx8eYc1Y3mKL7UhSag0kIj7pdkYf0v1p"
    }

    suspend fun getFactAndPicture(): CatInfo = withContext(Dispatchers.IO) {
        val fact = async { factRepository.getFact() }
        val picture = async { podRepository.getPicture(apiKey = API_KEY) }
        CatInfo(fact = fact.await().fact, pictureUrl = picture.await().url ?: "")
    }
}
