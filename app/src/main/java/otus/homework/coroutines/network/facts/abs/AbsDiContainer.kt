package otus.homework.coroutines.network.facts.abs

import retrofit2.Retrofit

abstract class AbsDiContainer {
    abstract val retrofit: Retrofit
    abstract val service: AbsCatService
}