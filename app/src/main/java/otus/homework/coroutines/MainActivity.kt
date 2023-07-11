package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private val scope by lazy { CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine")) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PresenterViewModel::class.java]

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        attachView(view)
        view.callback = {
            viewModel.scope.cancel()
            downloadData()
        }
        downloadData()
        viewModel.data.observe(this){
            _catsView?.populate(it)

        }



    }
    private fun downloadData(){
        scope.launch {
            viewModel.getDataFromNet(PresenterViewModel.Companion.DataType.FACT)
            viewModel.getDataFromNet(PresenterViewModel.Companion.DataType.CAT_IMAGE)
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
        scope.cancel()
        Log.d("CatsCoroutine", "Coroutine canceled" )

    }
}