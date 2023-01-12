package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class MeowImage(
    @field:SerializedName("file")
    val url: String
)