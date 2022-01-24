package otus.homework.coroutines

import otus.homework.coroutines.dto.Fact
import otus.homework.coroutines.dto.ImageFile
import retrofit2.http.GET

interface CatsService {

    companion object{
        private const val CAT_IMAGE_URL = "https://aws.random.cat/meow"
    }
    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET(CAT_IMAGE_URL)
    suspend fun getCatImage() : ImageFile
}