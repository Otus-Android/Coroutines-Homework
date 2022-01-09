package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class ImgResource(
	@field:SerializedName("file")
	val fileUrl: String
)
