package otus.homework.coroutines.di

import android.content.Context
import otus.homework.coroutines.IResourceProvider
import otus.homework.coroutines.ResourceProvider
import otus.homework.coroutines.service.CatFactService
import otus.homework.coroutines.service.CatPictureService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiContainer {

    private val retrofitCatFact by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitCatPicture by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catFactService: CatFactService by lazy { retrofitCatFact.create(CatFactService::class.java) }
    val catPictureService: CatPictureService by lazy { retrofitCatPicture.create(CatPictureService::class.java) }

    fun getResources(context: Context): IResourceProvider = ResourceProvider(context)
}