package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class FactImageResponse(
	val fact: Fact? = null,
	val image: Image? = null,
	var isSuccessful: Boolean? = true,
	var errorMessage: String? = null
)