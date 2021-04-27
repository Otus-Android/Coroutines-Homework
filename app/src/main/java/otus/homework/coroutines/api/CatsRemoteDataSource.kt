package otus.homework.coroutines.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CatsRemoteDataSource constructor(
    private val catsServiceFact: CatsServiceFact,
    private val catsServiceImage: CatsServiceImage,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getCatImage(): Result<CatImage> = withContext(dispatcher) {
        val response = catsServiceImage.getCatImage()
        if (response.isSuccessful) {
            return@withContext response.body()?.let { Result.Success(it) }
                ?: Result.Error(Exception(CAT_RESPONSE_ERROR))
        }
        return@withContext Result.Error(Exception(CAT_RESPONSE_ERROR))
    }

    suspend fun getCatFact(): Result<Fact> = withContext(dispatcher) {
        val response = catsServiceFact.getCatFact()
        if (response.isSuccessful) {
            return@withContext response.body()?.let { Result.Success(it) }
                ?: Result.Error(Exception(CAT_RESPONSE_ERROR))
        }
        return@withContext Result.Error(Exception(CAT_RESPONSE_ERROR))
    }

    companion object {
        private const val CAT_RESPONSE_ERROR = "Не удалось получить данные"
    }
}