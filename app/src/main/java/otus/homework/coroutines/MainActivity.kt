package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.view_models.PresenterViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PresenterViewModel
    private var _catsView: ICatsView? = null
    private lateinit var view: CatsView
    private var scope: CoroutineScope? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PresenterViewModel::class.java]
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
        setContentView(view)
        attachView(view)
        downloadData()
        viewModel.data.observe(this) {
            _catsView?.populate(it)

        }


    }

    private fun moreFactsButtonOnClick() {
        view.callback = {
            viewModel.scope.cancel()
            downloadData()
        }
    }

    private fun downloadData() {
        scope?.launch {
            viewModel.getDataFromNet(DataType.FACT)
            viewModel.getDataFromNet(DataType.CAT_IMAGE)
        }

    }

    private fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    private fun detachView() {
        _catsView = null

    }

    override fun onStop() {
        if (isFinishing) {
            detachView()
        }
        super.onStop()
        scope?.cancel()


    }

    override fun onResume() {
        scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
        super.onResume()
        moreFactsButtonOnClick()


    }
}
