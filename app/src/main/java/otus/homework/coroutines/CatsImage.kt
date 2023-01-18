package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatsImage(
    @field: SerializedName("file")
    val url: String
)
