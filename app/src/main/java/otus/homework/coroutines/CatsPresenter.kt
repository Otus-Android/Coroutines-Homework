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

    private val coroutineScope = CoroutineScope(CoroutineName("CatsCoroutine"))

   fun onInitComplete(context: Context){
     coroutineScope.launch {
           try {
               val fact = requestCatsFact()
               val picture = requestCatsPicture()
               if(fact != null && picture != null){
                   withContext(Dispatchers.Main){
                       _catsView?.populate(fact, picture)
                   }
               } else {
                   CrashMonitor.trackWarning()
               }
           } catch (e: Exception){
               if (e is SocketTimeoutException){
                   withContext(Dispatchers.Main){
                       Toast.makeText(context, "Не удалось получить ответ от сервером" , Toast.LENGTH_SHORT).show()
                   }
               } else{
                   CrashMonitor.trackWarning()
                   withContext(Dispatchers.Main){
                       Toast.makeText(context, "${e.message}" , Toast.LENGTH_SHORT).show()
                   }
               }
           }
       }
   }

    private suspend fun requestCatsFact(): Fact?{
       val async = coroutineScope.async {
            val responseFact = catsService.getCatFact()
            if(responseFact.isSuccessful && responseFact.body() != null){
                Log.d("TEST_TAG", "fact: ${responseFact.body()}")
                return@async responseFact.body()
            } else {
                CrashMonitor.trackWarning()
                return@async null
            }
        }
       return async.await()
    }

    private suspend fun requestCatsPicture(): Picture?{
        val async = coroutineScope.async {
            val responsePicture = catsServiceMeow.getCatRandomPicture()
            if(responsePicture.isSuccessful && responsePicture.body() != null){
                Log.d("TEST_TAG", "picture: ${responsePicture.body()}")
                return@async responsePicture.body()
            } else {
                CrashMonitor.trackWarning()
                return@async null
            }
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