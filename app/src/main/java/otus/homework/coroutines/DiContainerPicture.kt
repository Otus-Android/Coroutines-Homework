package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainerPicture {

    private val retrofitPicture by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val servicePicture by lazy { retrofitPicture.create(CatsServicePicture::class.java) }

}