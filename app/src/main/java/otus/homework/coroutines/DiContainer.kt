package otus.homework.coroutines

import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageCatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    private val retrofitImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceImage by lazy { retrofitImage.create(ImageCatsService::class.java) }

    val viewModel by lazy {
        CatsViewModel(
            catsService = service,
            imageCatsService = serviceImage
        )
    }
}