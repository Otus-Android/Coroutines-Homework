package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

data class CatPic(
    @field:SerializedName("file")
    val url: String
)
