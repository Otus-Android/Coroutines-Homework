package otus.homework.coroutines.data

import otus.homework.coroutines.data.model.Fact

class CatsRepositoryImpl(
    private val service: CatsService,
) : CatsRepository {

    override suspend fun getCatFact(): Result<Fact> {
        return try {
            Result.Success(model = service.getCatFact())
        } catch (exception: Exception) {
            Result.Error(exception = exception)
        }
    }
}