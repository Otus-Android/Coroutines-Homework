package otus.homework.coroutines.domain

class GetImageUseCase (private val catsRepository: CatsRepository<Result<Any>>) {
    suspend fun getImage():Any{
        return catsRepository.getImage()
    }
}