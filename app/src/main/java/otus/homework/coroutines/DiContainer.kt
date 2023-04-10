package otus.homework.coroutines

import com.squareup.picasso.Picasso
import otus.homework.coroutines.service.CatFactService
import otus.homework.coroutines.service.CatImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catFactRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val catImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catFactService: CatFactService by lazy { catFactRetrofit.create(CatFactService::class.java) }

    val catImageService: CatImageService by lazy { catImageRetrofit.create(CatImageService::class.java) }

    val crashMonitor: CrashMonitor by lazy { CrashMonitor }

    val imageLoader: ImageLoader by lazy { PicassoLoader(Picasso.get()) }
}