package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsServiceMeow: CatsServiceMeow
) {

    private var _catsView: ICatsView? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + Job())

   fun onInitComplete(context: Context){
     coroutineScope.launch {
           try {
               val fact = requestCatsFact()
               val picture = requestCatsPicture()
                       _catsView?.populate(fact, picture)

           } catch (e: Exception){
               if (e is SocketTimeoutException){
                   Toast.makeText(context, "Не удалось получить ответ от сервером" , Toast.LENGTH_SHORT).show()
               } else{
                   CrashMonitor.trackWarning()
                   Toast.makeText(context, "${e.message}" , Toast.LENGTH_SHORT).show()
               }
           }
       }
   }

    private suspend fun requestCatsFact(): Fact{
       val async = coroutineScope.async {
           return@async catsService.getCatFact()
        }
       return async.await()
    }

    private suspend fun requestCatsPicture(): Picture{
        val async = coroutineScope.async {
            return@async catsServiceMeow.getCatRandomPicture()
        }
        return async.await()
    }



    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        coroutineScope.cancel()
    }
}