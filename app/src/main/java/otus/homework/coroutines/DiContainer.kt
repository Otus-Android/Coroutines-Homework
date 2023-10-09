package otus.homework.coroutines

import otus.homework.coroutines.repo.Repository
import otus.homework.coroutines.repo.RepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitForFacts by lazy {
        Retrofit.Builder()
            .baseUrl(FACTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val factService by lazy { retrofitForFacts.create(CatFactService::class.java) }

//    private val gson = GsonBuilder().setLenient().create()
    private val retrofitForPics by lazy {
        Retrofit.Builder()
            .baseUrl(PICS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val picService by lazy { retrofitForPics.create(CatPicService::class.java) }

    val repository: Repository by lazy {RepositoryImpl(factService, picService)}

//      TODO picasso di
//    val picasso by lazy { Picasso.get() }

    companion object {
        private const val FACTS_BASE_URL = "https://catfact.ninja/"
        private const val PICS_BASE_URL = "https://api.thecatapi.com/"

    }
}