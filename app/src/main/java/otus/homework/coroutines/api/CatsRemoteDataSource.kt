package otus.homework.coroutines.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import otus.homework.coroutines.CatInfo

class CatsRemoteDataSource constructor(
    private val catsServiceFact: CatsServiceFact,
    private val catsServiceImage: CatsServiceImage,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getCatInfo(): Result<CatInfo> = withContext(dispatcher) {
        return@withContext Result.Success(
            CatInfo(
                catsServiceFact.getCatFact(),
                catsServiceImage.getCatImage().getImageUrl()
            )
        )
    }
}