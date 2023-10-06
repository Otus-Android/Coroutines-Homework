package otus.homework.coroutines.data.reposiroty

import otus.homework.coroutines.data.Result
import otus.homework.coroutines.data.api.CatsApi
import otus.homework.coroutines.data.mapper.FactMapper
import otus.homework.coroutines.domain.model.FactModel
import otus.homework.coroutines.domain.repository.FactRepository
import java.net.SocketTimeoutException

class FactRepositoryImpl(
    private val api: CatsApi,
    private val mapper: FactMapper,
) : FactRepository {

    override suspend fun getCatFact(): Result<FactModel> {
        return try {
            Result.Success(model = mapper.map(api.getCatFact()))
        } catch (exception: SocketTimeoutException) {
            Result.TimeoutError()
        }
    }
}