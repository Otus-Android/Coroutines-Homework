package otus.homework.coroutines.domain

interface CatsRepository {
    suspend fun getCatRandomFact(): Result<CatRandomFact>
}