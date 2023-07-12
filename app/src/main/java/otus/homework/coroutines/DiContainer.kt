package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer(private val url: String) {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }

    companion object {
        const val FACT_URL = "https://catfact.ninja/"

        //const val IMAGE_URL = "https://aws.random.cat/"
        const val IMAGE_URL = "https://testing-server-indol.vercel.app/"

    }
}
