package otus.homework.coroutines.ui

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import otus.homework.coroutines.utils.DispatchersProvider
import kotlin.coroutines.CoroutineContext

open class PresenterCoroutineScope(name: String) : CoroutineScope {
  override val coroutineContext: CoroutineContext =
    Dispatchers.Main +
      CoroutineName(name)

  fun io() = DispatchersProvider.io()
}