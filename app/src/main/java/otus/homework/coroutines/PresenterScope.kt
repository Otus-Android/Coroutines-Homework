package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + CoroutineName("CatsCoroutine")

    fun destroy(){
        Log.i("11111", "destroy: ")
        this.cancel()
    }
}