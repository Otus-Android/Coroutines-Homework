package otus.homework.coroutines

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CatFact(val fact: String?, val catImage: String?): Parcelable

data class Fact(
	val text: String?

)

data class Cat(
	val id: String?,
	val url: String?,
)

