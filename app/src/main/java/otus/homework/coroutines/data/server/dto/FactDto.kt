package otus.homework.coroutines.data.server.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FactDto(
	@field:SerializedName("fact")
	val fact: String,
	@field:SerializedName("length")
	val length: Int
): Parcelable