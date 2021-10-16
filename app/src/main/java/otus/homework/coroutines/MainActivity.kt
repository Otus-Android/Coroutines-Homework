package otus.homework.coroutines

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.domain.Error
import otus.homework.coroutines.domain.ICatRepository
import otus.homework.coroutines.domain.INetworkExceptionHandler
import otus.homework.coroutines.domain.Success
import otus.homework.coroutines.presenation.presenter.CatsPresenter
import otus.homework.coroutines.presenation.view.CatsView


class MainActivity : AppCompatActivity() {

	lateinit var catsPresenter: CatsPresenter


	private var rootView: View? = null
	private var progressView: View? = null

	private val viewModel by viewModels<CatsViewModel> {
		object : ViewModelProvider.Factory {
			override fun <T : ViewModel?> create(modelClass: Class<T>): T {
				return try {
					modelClass.getConstructor(
						ICatRepository::class.java,
						INetworkExceptionHandler::class.java,
					).newInstance(
						DiContainer.catsRepository,
						DiContainer.crashMonitorExceptionHandler()
					)
				} catch (e: InstantiationException) {
					throw RuntimeException("Cannot create an instance of $modelClass", e)
				} catch (e: IllegalAccessException) {
					throw RuntimeException("Cannot create an instance of $modelClass", e)
				}
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
		setContentView(view)
		view.onButtonClick = viewModel::getCatsFact
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
				is Success -> (rootView as? CatsView)?.populate(result.data)

				is Error -> DiContainer
					.uiExceptionHandler(applicationContext)
					.handleException(result.t)
			}
		}

		viewModel.isShowProgressLiveData.observe(this) { isLoading ->
			progressView?.isVisible = isLoading
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