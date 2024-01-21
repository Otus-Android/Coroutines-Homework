package otus.homework.coroutines.api.services.photos

import otus.homework.coroutines.api.IRetrofitService
import otus.homework.coroutines.dtos.Photo
import retrofit2.Response

interface IPhotoService : IRetrofitService {
    suspend fun getRandomPhoto() : Response<List<Photo>>
}
