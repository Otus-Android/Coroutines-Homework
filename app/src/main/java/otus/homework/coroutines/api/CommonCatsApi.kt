package otus.homework.coroutines.api

import otus.homework.coroutines.api.services.facts.CommonFactsService
import otus.homework.coroutines.api.services.photos.CommonPhotoService
import otus.homework.coroutines.api.services.photos.IPhotoService

@Suppress("SpellCheckingInspection")
object CommonCatsApi : BaseServiceApi(), ICatsApi {

    override val protocol = "https"
    override val host = "catfact.ninja"

    override val factsService: IServiceApi by lazy {
        CommonRetrofitServiceApi (
            serviceClass = CommonFactsService::class.java,
            httpHost = httpHost
        )
    }

    override val photoService: IServiceApi by lazy {
        object : BaseServiceApi(), IRetrofitServiceApi {
            override val serviceClass: Class<out IPhotoService> = CommonPhotoService::class.java
            override val protocol : String = "https"
            override val host: String = "api.thecatapi.com"
        }
    }

}
