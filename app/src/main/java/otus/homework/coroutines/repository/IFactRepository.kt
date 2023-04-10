package otus.homework.coroutines

import otus.homework.coroutines.model.Fact

interface IFactRepository {

    suspend fun getFactAsync() : Fact
}
