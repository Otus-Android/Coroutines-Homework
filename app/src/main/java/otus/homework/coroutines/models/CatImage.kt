package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class CatImage(
    @SerializedName("file")
    val imageString: String
)
