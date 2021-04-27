package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import otus.homework.coroutines.api.CatsRemoteDataSource
import otus.homework.coroutines.api.CatsServiceFact
import otus.homework.coroutines.api.CatsServiceImage
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

    private val serviceFact by lazy { retrofitFact.create(CatsServiceFact::class.java) }
    private val serviceImage by lazy { retrofitImage.create(CatsServiceImage::class.java) }

    val catsRemoteDateSource by lazy { CatsRemoteDataSource(serviceFact, serviceImage, Dispatchers.IO) }
}