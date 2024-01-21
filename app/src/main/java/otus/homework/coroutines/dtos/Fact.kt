package otus.homework.coroutines.dtos

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("fact")
	val fact: String,
	@field:SerializedName("length")
	val length: Int
)