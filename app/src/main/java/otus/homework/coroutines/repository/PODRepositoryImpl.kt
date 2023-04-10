package otus.homework.coroutines.repository

import otus.homework.coroutines.api.PODService
import otus.homework.coroutines.model.PODServerResponseData

class PODRepositoryImpl(private val service: PODService) : IPODRepository {

    override suspend fun sendServerRequest(apiKey: String): PODServerResponseData =
        service.getPictureOfTheDayByDateAsync(apiKey = apiKey).await()

}
