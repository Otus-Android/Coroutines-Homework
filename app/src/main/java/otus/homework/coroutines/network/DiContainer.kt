package otus.homework.coroutines.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    fun choiceUrlForRetrofit(isFact: Boolean): Any {
        val urlForRetrofit = if (isFact) "https://catfact.ninja/" else "https://catfact.ninja/"
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
}