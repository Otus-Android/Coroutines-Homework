package otus.homework.coroutines

import otus.homework.coroutines.models.CatsFact
import retrofit2.http.GET

interface CatsService {

    @GET("/fact?max_length=140")
    suspend fun getCatFact() : CatsFact
}