package otus.homework.coroutines.presenation.presenter

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CatsPresenterScope : CoroutineScope {

	override val coroutineContext: CoroutineContext =
		Dispatchers.Main + CoroutineName("CatsCoroutine")
}
