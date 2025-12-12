package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val onShowToast: (String?) -> Unit
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val fact = async {catsService.getCatFact()}
                val image = async {imageService.getCatImage().firstOrNull()}
                _catsView?.populate(CatModel(fact.await().fact, image.await()?.url))
            }
            catch (e: SocketTimeoutException)
            {
                onShowToast("Не удалось получить ответ от сервера")
            }
            catch (e: Exception)
            {
                onShowToast(e.message)
                CrashMonitor.trackWarning(e.message)
            }
        }
/*
        catsService.getCatFact().enqueue(object : Callback<Fact> {

            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
                if (response.isSuccessful && response.body() != null) {
                    _catsView?.populate(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Fact>, t: Throwable) {
                CrashMonitor.trackWarning()
            }
        })*/
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop()
    {
        job?.cancel()
    }
}