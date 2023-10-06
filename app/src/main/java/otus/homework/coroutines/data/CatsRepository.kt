package otus.homework.coroutines.data

import otus.homework.coroutines.data.model.Fact

interface CatsRepository {

    suspend fun getCatFact(): Result<Fact>
}