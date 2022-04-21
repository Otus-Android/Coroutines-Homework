package otus.homework.coroutines

import otus.homework.coroutines.api.CatFactService
import otus.homework.coroutines.api.CatPhotoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun createRetrofitInstance(url: String) = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val factRetrofitInstance = createRetrofitInstance(CAT_FACT_SERVICE_URL)
    private val photoRetrofitInstance = createRetrofitInstance(CAT_PHOTO_SERVICE_URL)

    val factService: CatFactService by lazy { factRetrofitInstance.create(CatFactService::class.java) }
    val photoService: CatPhotoService by lazy { photoRetrofitInstance.create(CatPhotoService::class.java) }

    companion object {
        const val CAT_FACT_SERVICE_URL = "https://cat-fact.herokuapp.com/facts/"
        const val CAT_PHOTO_SERVICE_URL = "https://aws.random.cat/"
    }
}