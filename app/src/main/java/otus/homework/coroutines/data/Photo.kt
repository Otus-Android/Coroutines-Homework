package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("file")
    val photoUrl: String
)