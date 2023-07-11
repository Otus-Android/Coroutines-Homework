package otus.homework.coroutines.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    fun choiceUrlForRetrofit(isFact: Boolean): Any {
        val urlForRetrofit = if (isFact) API_FOR_FACT else API_FOR_IMAGE
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(urlForRetrofit)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val service by lazy {
            if (isFact) {
                retrofit.create(CatsService::class.java)
            }
            else {
                retrofit.create(CatsImageService::class.java)
            }
        }
        return service
    }

    companion object {
        const val API_FOR_FACT = "https://catfact.ninja/"
        const val API_FOR_IMAGE = "https://api.thecatapi.com/v1/"
    }

}
