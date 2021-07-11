package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

/**
 * @author Юрий Польщиков on 11.07.2021
 */
data class FactImage(
    @field:SerializedName("file") var file: String
)