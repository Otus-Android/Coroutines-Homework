package otus.homework.coroutines.usecase

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

    suspend fun getFactAndPicture(): CatInfo {
        val fact = factRepository.getFactAsync()
        val picture = podRepository.getPictureAsync(apiKey = API_KEY)
        return CatInfo(fact = fact.await().fact, pictureUrl = picture.await().url ?: "")
    }
}
