package otus.homework.coroutines.data.reposiroty

import otus.homework.coroutines.data.Result
import otus.homework.coroutines.data.api.ImageUrlGenerateApi
import otus.homework.coroutines.data.mapper.ImageUrlMapper
import otus.homework.coroutines.domain.model.ImageUrlModel
import otus.homework.coroutines.domain.repository.ImageUrlRepository
import java.net.SocketTimeoutException

class ImageUrlRepositoryImpl(
    private val api: ImageUrlGenerateApi,
    private val mapper: ImageUrlMapper,
): ImageUrlRepository {

    override suspend fun getImageUrl(): Result<ImageUrlModel> {
        val model = api.getImageUrl()
        return try {
            Result.Success(model = mapper.map(model.first()))
        } catch (exception: SocketTimeoutException) {
            Result.TimeoutError()
        }
    }
}