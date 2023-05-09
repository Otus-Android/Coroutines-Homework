package otus.homework.coroutines.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import otus.homework.coroutines.api.PODService
import otus.homework.coroutines.model.PODServerResponseData

class PODRepositoryImpl(private val service: PODService) : IPODRepository {

    override suspend fun getPictureAsync(apiKey: String) =  withContext(Dispatchers.IO) {
        async { service.getPictureOfTheDayByDate(apiKey) }
    }

}
