package otus.homework.coroutines

import retrofit2.http.GET

interface CatFactService {
    @GET("fact")
    suspend fun getCatFact() : Fact
}

interface CatPicService {
    @GET("v1/images/search")
    suspend fun getCatPic() : List<Pic>
}