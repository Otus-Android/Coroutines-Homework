package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import otus.homework.coroutines.api.CommonCatsApi
import otus.homework.coroutines.api.ICatsApi
import otus.homework.coroutines.api.services.facts.IFactsService
import otus.homework.coroutines.api.services.photos.IPhotoService
import otus.homework.coroutines.api.toRetrofit
import otus.homework.coroutines.util.dangerCast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catsApi: ICatsApi by lazy { CommonCatsApi }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(catsApi.httpHost)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factsService: IFactsService by lazy {
        catsApi.factsService.run {
            retrofit.create(toRetrofit().serviceClass.dangerCast())
        }
    }

    val photoService: IPhotoService by lazy {
        catsApi.photoService.run {
            retrofit.newBuilder()
                .baseUrl(httpHost)
                .build()
                .create(toRetrofit().serviceClass.dangerCast())
        }
    }

    val presenterScope by lazy {
        CoroutineScope(Dispatchers.Main.immediate + CoroutineName("CatsCoroutine") + SupervisorJob())
    }
}
