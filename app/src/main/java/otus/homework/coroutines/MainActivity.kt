package otus.homework.coroutines

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.domain.*
import otus.homework.coroutines.presenation.presenter.CatsPresenter
import otus.homework.coroutines.presenation.view.CatsView


class MainActivity : AppCompatActivity() {

	lateinit var catsPresenter: CatsPresenter


	private var rootView: CatsView? = null
	private var progressView: View? = null

	private val viewModel by viewModels<CatsViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		rootView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
		setContentView(rootView)
		rootView?.onButtonClick = viewModel::getCatsFact
		//todo:раскомментировать, чтобы проверить презентер(задания 1,2)
		/*	 catsPresenter = CatsPresenter(DiContainer.catsFactService(), DiContainer.catsImageService())
			view.presenter = catsPresenter
			view.exceptionHandler = DiContainer.uiExceptionHandler(applicationContext)
			catsPresenter.attachView(view)
			catsPresenter.getCatsFact()
			rootView = view*/
	}

	override fun onStart() {
		super.onStart()
		/*todo:закоментировать, чтобы проверить презентер (задания 1,2) */
		initSubscriptions()
		initProgressView()
		viewModel.getCatsFact()
	}

	private fun initSubscriptions() {
		viewModel.catsCardLiveData.observe(this) { result ->
			when (result) {
				is Success -> rootView?.populate(result.data)

				is Error -> DiContainer
					.uiExceptionHandler(applicationContext)
					.handleException(result.t)

				is Processing -> {
					progressView?.isVisible = result.inProcess
				}
			}
		}
	}

	private fun initProgressView() {
		progressView = findViewById(R.id.progressView)
	}

	override fun onStop() {
		if (isFinishing) {
			catsPresenter.detachView()
		}
		super.onStop()
	}
}