package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("search")
//    @GET("meow")
    suspend fun getCatImage():  List<CatsImage>


    @GET("fact")
    suspend fun getCatFact(): Fact

}


