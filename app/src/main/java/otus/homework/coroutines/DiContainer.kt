package otus.homework.coroutines

import otus.homework.coroutines.network.services.FactService
import otus.homework.coroutines.network.services.ImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    companion object {
        private const val FACT_SERVICE_URL = "https://cat-fact.herokuapp.com/facts/"
        private const val IMAGE_SERVICE_URL = "https://aws.random.cat/"
    }

    private fun createRetrofitClient(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: FactService by lazy {
        createRetrofitClient(FACT_SERVICE_URL).create(FactService::class.java)
    }

    val imageService: ImageService by lazy {
        createRetrofitClient(IMAGE_SERVICE_URL).create(ImageService::class.java)
    }
}