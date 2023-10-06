package otus.homework.coroutines.presentation

import otus.homework.coroutines.data.model.Fact

interface FactsRepository {
    suspend fun getFact(): String
}