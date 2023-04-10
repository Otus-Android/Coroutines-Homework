package otus.homework.coroutines.repository

import otus.homework.coroutines.model.PODServerResponseData

interface IPODRepository {

    suspend fun sendServerRequest(apiKey: String) : PODServerResponseData

}
