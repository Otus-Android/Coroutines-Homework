package otus.homework.coroutines.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import otus.homework.coroutines.ui.CatsData

class CatsRepository(
    private val catsService: CatsService,
    private val picsService: PicsService,
) {

    suspend fun getCatsData(): CatsData = coroutineScope {
        val catFactDeferred = async { catsService.getCatFact() }
        val picDeferred = async { picsService.getPicture() }

        val catFactResponse = catFactDeferred.await()
        val picResponse = picDeferred.await()

        if (catFactResponse.isSuccessful && catFactResponse.body() != null &&
            picResponse.isSuccessful && picResponse.body() != null
        ) {
            CatsData(
                fact = catFactResponse.body()!!.fact,
                pictureUrl = picResponse.body()!!.first().url,
            )
        } else {
            throw IllegalArgumentException("cannot parse server response")
        }
    }

}