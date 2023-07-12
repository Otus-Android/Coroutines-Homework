package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

// https://catfact.ninja/#/Facts/getFacts
data class Fact(

	@field:SerializedName("fact")
	val fact: String,

	@field:SerializedName("length")
	val length: String
)