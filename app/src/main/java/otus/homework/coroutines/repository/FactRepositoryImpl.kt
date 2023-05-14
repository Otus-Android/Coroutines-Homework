package otus.homework.coroutines.repository

import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.model.Fact

class FactRepositoryImpl(private val service: CatsService) : IFactRepository {

    override suspend fun getFact(): Fact = service.getCatFact()

}
