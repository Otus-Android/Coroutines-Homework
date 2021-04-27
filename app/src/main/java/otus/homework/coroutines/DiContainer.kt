package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitFact by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://thatcopy.pw/catapi/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceFact by lazy { retrofitFact.create(CatsServiceFact::class.java) }
    val serviceImage by lazy { retrofitImage.create(CatsServiceImage::class.java) }
}