package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

/**
 * @author Kovrizhkin V. on 07.11.2021
 */
data class Image(
    @SerializedName("file") val path: String
)