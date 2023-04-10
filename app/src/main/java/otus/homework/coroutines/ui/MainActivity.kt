package otus.homework.coroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.R
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private lateinit var viewModel: MainViewModel

    private lateinit var catInfoView: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catInfoView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catInfoView)

        viewModel = MainViewModel.Factory(catInfoUseCase = diContainer.catInfoUseCase).create(MainViewModel::class.java)
        viewModel.subscribeToLiveData().observe(this,  this::renderData)

        catInfoView.addOnClickListener { viewModel.getCatInfo() }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getCatInfo()
    }

    private fun renderData(state: CatInfoState?) {
        when(state) {
            is CatInfoState.Success -> {
                catInfoView.populate(catInfo = state.serverResponseData)
            }
            is CatInfoState.Error -> {
                handleError(throwable = state.error)
            }
            else -> Unit
        }
    }

    private fun handleError(throwable: Throwable) {
        when(throwable) {
            is SocketTimeoutException -> catInfoView.showTimeoutMessage()
            else -> catInfoView.showMessage(throwable.message)
        }
    }
}
