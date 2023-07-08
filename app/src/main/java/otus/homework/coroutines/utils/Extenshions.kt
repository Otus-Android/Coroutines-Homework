package otus.homework.coroutines.utils

import otus.homework.coroutines.network.models.CatsImage
import otus.homework.coroutines.network.models.Fact

fun Fact?.orEmpty() = this  ?: Fact(fact = "", length = 0)
fun CatsImage?.orEmpty() = this  ?: CatsImage(url = "")