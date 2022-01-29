package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET
    suspend fun getCatFact(@Url url: String = "https://cat-fact.herokuapp.com/facts/random?animal_type=cat") : Fact

    @GET
    suspend fun getCatPicture(@Url url: String = "https://aws.random.cat/meow") : Picture
}
