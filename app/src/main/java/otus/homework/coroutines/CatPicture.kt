package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatPicture(
    @field:SerializedName("file")
    val fileUrl: String
)