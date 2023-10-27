package otus.homework.coroutines

import otus.homework.coroutines.models.CatImage
import otus.homework.coroutines.models.CatFact
import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact(): CatFact
}

interface CatsImageService {

    @GET("images/search")
    suspend fun getCatsImages(): List<CatImage>
}