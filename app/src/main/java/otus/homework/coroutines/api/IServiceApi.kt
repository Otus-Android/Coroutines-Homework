package otus.homework.coroutines.api

import android.net.Uri
import java.lang.Exception

interface IServiceApi {
    val httpHost: String
}

interface IRetrofitServiceApi : IServiceApi {
    val serviceClass: Class<out IRetrofitService>
}

interface IRetrofitService

data class CommonRetrofitServiceApi<T : IRetrofitService>(
    override val httpHost: String,
    override val serviceClass: Class<T>
) : IRetrofitServiceApi

abstract class BaseServiceApi : IServiceApi {

    protected abstract val host: String
    protected abstract val protocol: String

    override val httpHost by lazy {
        Uri.Builder()
            .scheme(protocol)
            .authority(host)
            .build()
            .toString()
    }
}

fun IServiceApi.toRetrofit(): IRetrofitServiceApi {
    return try {
        this as IRetrofitServiceApi
    } catch (e: Exception) {
        throw IllegalStateException(
            "IServiceApi has different type of implementation: ${this.javaClass.simpleName}",
            e
        )
    }
}