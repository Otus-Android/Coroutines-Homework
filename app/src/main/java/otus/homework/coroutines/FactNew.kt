package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class FactNew(
	@field:SerializedName("length")
	val length: Int,
	@field:SerializedName("fact")
	val fact: String,
)