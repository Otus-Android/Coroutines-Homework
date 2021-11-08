package otus.homework.coroutines

interface Repository {
  suspend fun getCat(): Cat
}

class RepositoryImpl(
  private val catsService: CatsService,
  private val catMapper: Mapper
) : Repository {

  override suspend fun getCat(): Cat {
    val fact = catsService.getCatFact()
    val image = catsService.getCatImage()
    return catMapper.map(fact, image)
  }
}