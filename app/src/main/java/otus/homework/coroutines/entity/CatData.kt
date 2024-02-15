package otus.homework.coroutines.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import otus.homework.coroutines.data.server.dto.FactDto
import otus.homework.coroutines.data.server.dto.PhotoDto

@Parcelize
data class CatData(
    val factData: FactDto? = null,
    val photoData: PhotoDto? = null
): Parcelable
