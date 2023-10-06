package otus.homework.coroutines.domain.repository

import otus.homework.coroutines.data.Result
import otus.homework.coroutines.domain.model.FactModel

interface FactRepository {

    suspend fun getCatFact(): Result<FactModel>
}