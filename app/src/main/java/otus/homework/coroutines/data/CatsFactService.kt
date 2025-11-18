package otus.homework.coroutines.data

import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact(): CatsFact
}
