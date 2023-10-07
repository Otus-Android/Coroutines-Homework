//package otus.homework.coroutines.ui
//
//import kotlinx.coroutines.CoroutineName
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.async
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import otus.homework.coroutines.CrashMonitor
//import otus.homework.coroutines.presentation.CatContent
//import otus.homework.coroutines.presentation.FactsRepository
//import otus.homework.coroutines.presentation.PictureRepository
//import java.net.SocketTimeoutException
//
//class CatsPresenter(
//    private val factsRepository: FactsRepository,
//    private val pictureRepository: PictureRepository
//) {
//    private var job: Job? = null
//    private var _catsView: ICatsView? = null
//
//    fun onInitComplete() {
//        getFactsByCoroutines()
//    }
//
//    private fun getFactsByCoroutines() {
//        job = CoroutineScope(Dispatchers.IO + CoroutineName(CATS_COROUTINE_NAME)).launch {
//            try {
//                val jFact = async {
//                    factsRepository.getFact()
//                }
//                val jPic = async {
//                    pictureRepository.getImage()
//                }
//                val catContent = CatContent(
//                    fact = jFact.await(),
//                    image = jPic.await()
//                )
//                withContext(Dispatchers.Main) {
//                    _catsView?.populate(catContent)
//                }
//            } catch (ste: SocketTimeoutException) {
//                withContext(Dispatchers.Main) {
//                    _catsView?.showToast(TIME_OUT_EXCEPTION_MESSAGE)
//                }
//            } catch (e: Throwable) {
//                CrashMonitor.trackWarning()
//                withContext(Dispatchers.Main) {
//                    e.message?.let {
//                        _catsView?.showToast(it)
//                    }
//                }
//            }
//        }
//    }
//
//    fun attachView(catsView: ICatsView) {
//        _catsView = catsView
//    }
//
//    fun detachView() {
//        _catsView = null
//        job?.cancel()
//    }
//    companion object{
//        private const val TIME_OUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервером"
//        private const val CATS_COROUTINE_NAME = "CatsCoroutine"
//        private const val TAG = "TAG"
//    }
//}