package otus.homework.coroutines


import com.google.gson.annotations.SerializedName

class PicsResponse : ArrayList<PicsResponseItem>()

data class PicsResponseItem(
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)