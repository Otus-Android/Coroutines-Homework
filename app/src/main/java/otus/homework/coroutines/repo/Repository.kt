package otus.homework.coroutines.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import otus.homework.coroutines.CatFactService
import otus.homework.coroutines.CatPicService
import otus.homework.coroutines.MeowInfo
import otus.homework.coroutines.viewModel.Result

class RepositoryImpl(
    private val factService: CatFactService,
    private val picService: CatPicService
) : Repository {

    override suspend fun getMeowInfo(): Result<MeowInfo> {
        val scope = CoroutineScope(Job())

        val factDeferred = scope.async {
            factService.getCatFact()
        }
        val picDeferred = scope.async {
            picService.getCatPic()
        }
        return Result.tryWith {
            MeowInfo(
                factDeferred.await().fact,
                picDeferred.await().first().url
            )
        }

//        return try {
//            val fact = factDeferred.await()
//            val pic = picDeferred.await().first()
//            Result.Success(MeowInfo(fact.fact, pic.url))
//        } catch (e: CancellationException) {
//            throw e
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
    }
}

interface Repository {
    suspend fun getMeowInfo(): Result<MeowInfo>
}