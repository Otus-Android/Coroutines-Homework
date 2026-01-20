package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("fact")
	val fact: String,
	@field:SerializedName("length")
	val length: Int,

    val imageUrl: String? = null
){
    fun withImageUrl(imageUrl: String): Fact {
        return Fact(fact, length, imageUrl)
    }
}