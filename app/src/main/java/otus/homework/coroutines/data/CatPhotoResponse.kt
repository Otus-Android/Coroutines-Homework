package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class CatPhotoResponse(
    @SerializedName("file")
    val photoUrl: String
)
