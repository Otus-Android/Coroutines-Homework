package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Response<Fact>
}

interface CatsViewService {
    @GET("meow")
    suspend fun getCatView() : Response<ViewCat>
}