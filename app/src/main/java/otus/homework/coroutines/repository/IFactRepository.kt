package otus.homework.coroutines.repository

import otus.homework.coroutines.model.Fact

interface IFactRepository {

    suspend fun getFact() : Fact
}
