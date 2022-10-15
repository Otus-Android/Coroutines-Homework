package otus.homework.coroutines.network.facts.base

interface AbsCatService {
    suspend fun getCatFact(): AbsCatFact
}