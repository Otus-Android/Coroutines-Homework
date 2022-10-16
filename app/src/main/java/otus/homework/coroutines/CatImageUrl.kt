package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatImageUrl(
	@field:SerializedName("file")
	val fileUrl: String,
)