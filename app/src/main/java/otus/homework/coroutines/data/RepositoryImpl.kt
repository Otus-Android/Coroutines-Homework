package otus.homework.coroutines.data

import otus.homework.coroutines.domain.Repository

class RepositoryImpl(
    private val catsService: CatsService,
    private val imageService: ImageService
) : Repository {

    override suspend fun getFact(): String = catsService.getCatFact().fact

    override suspend fun getImageUrl(): String = imageService.getImage()[0].url
}
