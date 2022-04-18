package otus.homework.coroutines.data

interface CatsRepo {
    suspend fun getCatsFactsWithPhoto(): CatDto
}