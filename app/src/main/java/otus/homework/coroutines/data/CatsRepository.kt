package otus.homework.coroutines.data

import otus.homework.coroutines.ui.CatsData

class CatsRepository(
    private val catsService: CatsService,
    private val picsService: PicsService,
) {

    suspend fun getCatsData(): CatsData {
        val catFactResponse = catsService.getCatFact()
        val picResponse = picsService.getPicture()
        if (catFactResponse.isSuccessful && catFactResponse.body() != null &&
            picResponse.isSuccessful && picResponse.body() != null
        ) {
            return CatsData(
                fact = catFactResponse.body()!!.fact,
                pictureUrl = picResponse.body()!!.first().url,
            )
        } else {
            throw IllegalArgumentException("cannot parse server response")
        }
    }

}