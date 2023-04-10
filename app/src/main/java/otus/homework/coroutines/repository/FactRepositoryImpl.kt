package otus.homework.coroutines.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.model.Fact

class FactRepositoryImpl(private val service: CatsService): IFactRepository {
    override suspend fun getFactAsync(): Fact = withContext(Dispatchers.IO) {
        service.getCatFact().await()
    }

}
