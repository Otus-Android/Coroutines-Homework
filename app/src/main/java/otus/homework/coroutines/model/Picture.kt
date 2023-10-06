package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Picture(
    @SerializedName("hits")
    val hits: ArrayList<Hit>
)
