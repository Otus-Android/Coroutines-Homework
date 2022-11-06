package otus.homework.coroutines.data

import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    private companion object {
        const val PIC_URL = "https://aws.random.cat/meow"
    }

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact

    @GET
    suspend fun getPicUrl(@Url url: String = PIC_URL): PicUrl

}