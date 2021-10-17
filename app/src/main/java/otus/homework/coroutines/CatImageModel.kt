package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatImageModel(
    @field:SerializedName("file")
    val file: String
)