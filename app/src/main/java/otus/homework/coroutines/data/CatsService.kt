package otus.homework.coroutines.data

import otus.homework.coroutines.data.model.Fact
import retrofit2.Call
import retrofit2.http.GET

interface CatsService {

    @GET("/fact")
    suspend fun getCatFact(): Fact

    companion object {
        const val TIMEOUT_MESSAGE = "Не удалось получить ответ от сервером"
    }
}