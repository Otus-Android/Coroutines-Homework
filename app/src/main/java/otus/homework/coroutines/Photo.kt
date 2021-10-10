package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Photo(
    @field:SerializedName("file")
    val url: String
)
