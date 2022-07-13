package otus.homework.coroutines

import otus.homework.coroutines.api.CatFactsService
import otus.homework.coroutines.api.CatImagesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

  private val catFactRetrofit by lazy {
    Retrofit.Builder()
      .baseUrl("https://cat-fact.herokuapp.com/facts/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  private val catImageRetrofit by lazy {
    Retrofit.Builder()
      .baseUrl("https://aws.random.cat/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  val catFactService by lazy { catFactRetrofit.create(CatFactsService::class.java) }
  val catImageService by lazy { catImageRetrofit.create(CatImagesService::class.java) }
}