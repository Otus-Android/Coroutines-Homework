package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class ActivityResponse(
	@field:SerializedName("activity")
	val activity: String
)