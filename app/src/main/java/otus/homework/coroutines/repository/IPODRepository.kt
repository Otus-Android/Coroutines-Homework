package otus.homework.coroutines.repository

import otus.homework.coroutines.model.PODServerResponseData

interface IPODRepository {

    suspend fun getPicture(apiKey: String) : PODServerResponseData

}
