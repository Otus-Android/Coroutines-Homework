package otus.homework.coroutines.entities.api

import com.google.gson.annotations.SerializedName

data class CatPhoto(
    @field:SerializedName("file")
    val fileUrl: String
)