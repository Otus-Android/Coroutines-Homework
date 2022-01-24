package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class CatPicture(
    @field:SerializedName("file")
    val file: String
)