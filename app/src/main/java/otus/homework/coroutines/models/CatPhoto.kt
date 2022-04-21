package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class CatPhoto(
    @SerializedName("file")
    val photoUrl: String
)