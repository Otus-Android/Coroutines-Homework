package otus.homework.coroutines.presentation



interface FactsRepository {
    suspend fun getFact(): Result<String>
}