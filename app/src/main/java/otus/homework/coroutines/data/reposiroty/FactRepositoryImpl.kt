package otus.homework.coroutines.data.reposiroty

import otus.homework.coroutines.data.Result
import otus.homework.coroutines.data.api.FactApi
import otus.homework.coroutines.data.mapper.FactMapper
import otus.homework.coroutines.domain.model.FactModel
import otus.homework.coroutines.domain.repository.FactRepository
import java.net.SocketTimeoutException
import javax.inject.Inject

class FactRepositoryImpl @Inject constructor(
    private val api: FactApi,
    private val mapper: FactMapper,
) : FactRepository {

    override suspend fun getCatFact(): Result<FactModel> {
        return try {
            Result.Success(model = mapper.map(api.getFact()))
        } catch (exception: SocketTimeoutException) {
            Result.TimeoutError()
        }
    }
}