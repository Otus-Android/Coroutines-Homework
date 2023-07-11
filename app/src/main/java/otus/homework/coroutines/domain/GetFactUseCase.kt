package otus.homework.coroutines.domain

class GetFactUseCase(private val catsRepository: CatsRepository<Result<Any>>) {

    suspend fun getFact(): Any {
        return catsRepository.getFact()
    }
}
