package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

/**
 * @author o.s.terekhova
 */
data class Image(
    @field:SerializedName("file")
    val url: String
)
