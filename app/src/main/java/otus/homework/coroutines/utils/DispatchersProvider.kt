package otus.homework.coroutines.utils

import kotlinx.coroutines.Dispatchers

object DispatchersProvider {
  fun io() = Dispatchers.IO
}