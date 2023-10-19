package otus.homework.coroutines.presentation

import otus.homework.coroutines.presentation.model.FactModel


interface FactsRepository {
    suspend fun getFact(): Result<FactModel>
}