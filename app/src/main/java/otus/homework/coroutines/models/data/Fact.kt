package otus.homework.coroutines.models.data

import com.google.gson.annotations.SerializedName


/**
 * Модель факта
 *
 * @property fact описание факта
 */
// https://catfact.ninja/#/Facts/getFacts
data class Fact(

    @field:SerializedName("fact")
    val fact: String,

    @field:SerializedName("length")
    val length: String
)