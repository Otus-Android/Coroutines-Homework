package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class ImageFile(
    @SerializedName("file")
    val url: String
)
