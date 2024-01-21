package otus.homework.coroutines.api

interface ICatsApi : IServiceApi {
    val factsService: IServiceApi
    val photoService: IServiceApi
}