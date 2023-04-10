package otus.homework.coroutines.api

import kotlinx.coroutines.Deferred
import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    fun getCatFact() : Deferred<Fact>
}
