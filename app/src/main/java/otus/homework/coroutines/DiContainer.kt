package otus.homework.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import otus.homework.coroutines.network.CatsRepository
import otus.homework.coroutines.network.CatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object DiContainer {

    @Singleton
    @Provides
    fun provideRetrofit() :Retrofit {
       return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideService(retrofit: Retrofit): CatsService =
        retrofit.create(CatsService::class.java)

    @Singleton
    @Provides
    fun provideRepository(service: CatsService) =
        CatsRepository(service)

}