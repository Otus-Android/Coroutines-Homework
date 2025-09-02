//package otus.homework.coroutines
//
//import kotlinx.coroutines.CoroutineName
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.async
//import kotlinx.coroutines.launch
//import java.net.SocketTimeoutException
//import kotlin.coroutines.cancellation.CancellationException
//
//
//class CatsPresenter(
//    private val catsService: CatsService,
//    private val catsImagesService: CatsImageService,
//    private val presenterScope: CoroutineScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
//) {
//
//    private var _catsView: ICatsView? = null
//    private var job: Job? = null
//
//    fun onInitComplete() {
//    job = presenterScope.launch {
//        try {
//              val fact = async { catsService.getCatFact() }.await()
//              val catImage:CatImage = async { catsImagesService.getCatImage()[0] }.await()
//              _catsView?.populate(Cat(fact.toString(), catImage.url))
//
//        } catch (ex: Exception) {
//
//            if (ex is CancellationException) throw ex
//            when (ex) {
//
//                is SocketTimeoutException -> {
//                    _catsView?.onError("Unable to get response from server")
//                }
//                else -> {
//                  CrashMonitor.trackWarning(ex.message.toString())
//                    _catsView?.onError(ex.message.toString())
//                }
//            }
//        }
//    }
//    }
//
//    fun onStop() {
//        job?.cancel()
//    }
//
//
//     fun attachView(catsView: ICatsView) {
//        _catsView = catsView
//    }
//
//    fun detachView() {
//        _catsView = null
//    }
//}
