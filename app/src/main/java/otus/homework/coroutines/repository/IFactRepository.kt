package otus.homework.coroutines.repository

import kotlinx.coroutines.Deferred
import otus.homework.coroutines.model.Fact

interface IFactRepository {

    suspend fun getFactAsync() : Deferred<Fact>
}
