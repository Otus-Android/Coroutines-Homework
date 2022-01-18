package otus.homework.coroutines

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        val response = catsService.getCatFact()
        response.body()?.let {
            if (response.isSuccessful) {
                _catsView?.populate(it)
            } else {
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}