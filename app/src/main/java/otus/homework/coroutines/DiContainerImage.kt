package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainerImage {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.catы/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val imageService: ImageService by lazy { retrofit.create(ImageService::class.java) }
}