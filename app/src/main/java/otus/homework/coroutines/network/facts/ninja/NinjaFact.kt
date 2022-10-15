package otus.homework.coroutines.network.facts.ninja

import com.google.gson.annotations.SerializedName
import otus.homework.coroutines.network.facts.abs.AbsCatFact

data class NinjaFact(
    @field:SerializedName("fact")
    override val text: String,
    @field:SerializedName("length")
    val length: Int,
) : AbsCatFact() {
}