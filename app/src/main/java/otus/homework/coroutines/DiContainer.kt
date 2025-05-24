package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun retrofit(url: String) : Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val catsService: CatsService by lazy { retrofit(FACT).create(CatsService::class.java) }
    val imageCatsService: ImageCatsService by lazy { retrofit(IMAGE).create(ImageCatsService::class.java) }

    companion object {
        private const val FACT = "https://catfact.ninja/"
        private const val IMAGE = "https://api.thecatapi.com/"
    }
}