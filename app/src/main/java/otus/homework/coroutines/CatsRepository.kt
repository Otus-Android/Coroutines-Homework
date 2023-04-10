package otus.homework.coroutines

import android.util.Log

class CatsRepository(
    private val catsService: CatsService
) : BaseRepository() {

    suspend fun getCatFact() = getResult {
        catsService.getCatFact()
    }

    suspend fun getCatPic() = getResult {
        catsService.getCatPic()
    }

}