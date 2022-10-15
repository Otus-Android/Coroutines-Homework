package otus.homework.coroutines.network.facts.abs

interface AbsCatService {
    suspend fun getCatFact(): AbsCatFact
}