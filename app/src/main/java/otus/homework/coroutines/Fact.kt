package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("length")
	val length: Int,
	@field:SerializedName("fact")
	val fact: String
)