package otus.homework.coroutines.repository

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.model.Fact

class FactRepositoryImpl(private val service: CatsService): IFactRepository {

    override suspend fun getFactAsync(): Deferred<Fact> = withContext(Dispatchers.IO) {
        async { service.getCatFact() }
    }

}
