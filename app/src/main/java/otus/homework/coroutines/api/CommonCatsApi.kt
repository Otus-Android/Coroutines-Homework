package otus.homework.coroutines.api

import otus.homework.coroutines.api.services.facts.CommonFactsService
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
}
