package otus.homework.coroutines.data

import otus.homework.coroutines.models.data.Fact
import retrofit2.http.GET

/**
 * Сервис получения случайного факта
 */
interface FactService {

    /** Получить случайных факт о кошке */
    @GET("fact")
    suspend fun getCatFact(): Fact
}