package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("file")
    val picUrl: String? = null
)
