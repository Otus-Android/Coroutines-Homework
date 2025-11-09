package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact

}
interface CatsServicePics {
    @GET("v1/images/search")
    suspend fun getCatPicUrl() : List<CatImage>
}