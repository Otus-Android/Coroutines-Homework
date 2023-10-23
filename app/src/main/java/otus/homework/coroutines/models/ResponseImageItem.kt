package otus.homework.coroutines.models

import otus.homework.coroutines.Favourite

data class ResponseImageItem(
    val breeds: List<Any>,
    val favourite: Favourite,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)