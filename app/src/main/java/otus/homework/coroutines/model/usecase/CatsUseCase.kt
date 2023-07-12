package otus.homework.coroutines.model.usecase

import otus.homework.coroutines.model.entity.Fact
import otus.homework.coroutines.model.mapper.FactDtoToEntityMapper
import otus.homework.coroutines.model.repository.CatsRepository

class CatsUseCase(
    private val catsRepository: CatsRepository,
    private val factMapper: FactDtoToEntityMapper
) {

    suspend fun getCatFact(): Fact {
        val catFact = catsRepository.getCatFact()
        val catImage = catsRepository.getCatImage()

        return factMapper.toEntity(catFact, catImage)
    }
}
