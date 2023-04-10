package otus.homework.coroutines.network

import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val catsService: CatsService
) : BaseRepository() {

    suspend fun getCatFact() = getResult {
        catsService.getCatFact()
    }

    suspend fun getCatPic() = getResult {
        catsService.getCatPic()
    }

}