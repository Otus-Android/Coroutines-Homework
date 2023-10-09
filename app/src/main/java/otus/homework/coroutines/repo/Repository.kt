package otus.homework.coroutines.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import otus.homework.coroutines.CatFactService
import otus.homework.coroutines.CatPicService
import otus.homework.coroutines.MeowInfo

class RepositoryImpl(
    private val factService: CatFactService,
    private val picService: CatPicService
) : Repository {
    override suspend fun getMeowInfo(): MeowInfo {
        val scope = CoroutineScope(Job())

        val factDeferred = scope.async(Dispatchers.IO) {
            factService.getCatFact()
        }
        val picDeferred = scope.async(Dispatchers.IO) {
            picService.getCatPic()
        }

        val fact = factDeferred.await()
        val pic = picDeferred.await().first()

        return MeowInfo(fact.fact, pic.url)
    }
}

interface Repository {
    suspend fun getMeowInfo(): MeowInfo
}