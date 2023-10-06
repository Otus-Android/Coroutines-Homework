package otus.homework.coroutines.data.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("totalHits")
    val totalHits: Int,
    @SerializedName("hits")
    val hits: ArrayList<Hit>
)
