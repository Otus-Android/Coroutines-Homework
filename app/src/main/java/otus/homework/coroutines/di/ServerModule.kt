package otus.homework.coroutines.di

import dagger.Module
import dagger.Provides
import otus.homework.coroutines.data.server.CatsPhotosService
import otus.homework.coroutines.data.server.CatsTextService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ServerModule {

    @Provides
    fun getCatsPhotoService(): CatsPhotosService {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatsPhotosService::class.java)
    }

    @Provides
    fun getCatsTextService(): CatsTextService {
        return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatsTextService::class.java)
    }
}