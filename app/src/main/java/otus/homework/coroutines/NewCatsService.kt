package otus.homework.coroutines

import retrofit2.http.GET

interface NewCatsService {
    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET("meow")
    suspend fun getCatPicture(): CatPicture
}