package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

data class CatImg(
    @SerializedName("file")
    val image: String
)