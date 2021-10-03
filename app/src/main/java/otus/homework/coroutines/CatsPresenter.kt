//package otus.homework.coroutines
//
//import kotlinx.coroutines.*
//import retrofit2.Response
//import java.net.SocketTimeoutException
//import kotlin.coroutines.CoroutineContext
//
//class CatsPresenter(
//    private val catsService: CatsService
//) {
//    private var job = Job()
//    private val coroutineContext: CoroutineContext
//        get() = Dispatchers.Main + job + CoroutineName("CatsCoroutine")
//    private val presenterScope = CoroutineScope(coroutineContext)
//
//    private var _catsView: ICatsView? = null
//
//    fun onInitComplete() {
//        requestFact()
//    }
////        catsService.getCatFact().enqueue(object : Callback<Fact> {
////
////            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
////                if (response.isSuccessful && response.body() != null) {
////                    _catsView?.populate(response.body()!!)
////                }
////            }
////
////            override fun onFailure(call: Call<Fact>, t: Throwable) {
////                CrashMonitor.trackWarning()
////            }
////        })
////    }
//
//    fun requestFact() = presenterScope.launch {
//        try {
//            val factResponse = presenterScope.async { catsService.getCatFact() }
//            val imageResponse = presenterScope.async { catsService.getRandomCatImage() }
//
//            handleRemoteResponses(factResponse.await(), imageResponse.await())
//        } catch (exception: SocketTimeoutException) {
//            _catsView?.showMessage(R.string.connection_timeout)
//        } catch (exception: Exception) {
//            CrashMonitor.trackWarning(exception.message.orEmpty())
//        }
//    }
//
//    private fun handleRemoteResponses(
//        factResponse: Response<Fact>,
//        imageResponse: Response<RandomCatImage>
//    ) {
//        if (factResponse.isSuccessful && factResponse.body() != null &&
//            imageResponse.isSuccessful && imageResponse.body() != null
//        ) {
//            val catNew = CatNewPresentationModel(
//                checkNotNull(factResponse.body()).text,
//                checkNotNull(imageResponse.body()).file
//            )
//            _catsView?.populate(catNew)
//        } else CrashMonitor.trackWarning(factResponse.message())
//    }
//
//    fun attachView(catsView: ICatsView) {
//        _catsView = catsView
//    }
//
//    fun detachView() {
//        _catsView = null
//        presenterScope.cancel()
//    }
//}
//
//data class CatNewPresentationModel(
//    val factText: String,
//    val imageSource: String
//)