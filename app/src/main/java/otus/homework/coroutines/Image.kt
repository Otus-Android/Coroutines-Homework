package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("file")
    val link: String
)