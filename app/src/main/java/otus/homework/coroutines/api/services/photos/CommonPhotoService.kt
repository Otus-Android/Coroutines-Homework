package otus.homework.coroutines.api.services.photos

import otus.homework.coroutines.dtos.Photo
import retrofit2.Response
import retrofit2.http.GET

interface CommonPhotoService : IPhotoService {
    @GET("v1/images/search")
    override suspend fun getRandomPhoto(): Response<List<Photo>>
}
