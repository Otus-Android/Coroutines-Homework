package otus.homework.coroutines.data.models

import com.google.gson.annotations.SerializedName

/**
 * Модель изображения
 *
 * @property id идентификатор изображения
 * @property url ссылка на изображение
 */
// https://developers.thecatapi.com/view-account/ylX4blBYT9FaoVd6OhvR?report=bOoHBz-8t
data class Image(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("width")
    val width: String,

    @field:SerializedName("height")
    val height: String
)
