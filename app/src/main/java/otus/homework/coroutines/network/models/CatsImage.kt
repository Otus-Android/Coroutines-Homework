package otus.homework.coroutines.network.models

import com.google.gson.annotations.SerializedName

data class CatsImage(
    @field: SerializedName("url")
    val url:String = ""
)
