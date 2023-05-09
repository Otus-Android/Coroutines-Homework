package otus.homework.coroutines.repository

import kotlinx.coroutines.Deferred
import otus.homework.coroutines.model.PODServerResponseData

interface IPODRepository {

    suspend fun getPictureAsync(apiKey: String) : Deferred<PODServerResponseData>

}
