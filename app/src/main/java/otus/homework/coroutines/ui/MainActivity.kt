package otus.homework.coroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import otus.homework.coroutines.R.layout
import otus.homework.coroutines.ui.model.CatsResult.Failure
import otus.homework.coroutines.ui.model.CatsResult.Loading
import otus.homework.coroutines.ui.model.CatsResult.Success
import otus.homework.coroutines.utils.DiContainer

class MainActivity : AppCompatActivity() {

  lateinit var catsPresenter: CatsPresenter

  private val diContainer = DiContainer()
  private val vm: CatsViewModel by viewModels {
    CatsViewModelFactory(
      diContainer.catsService,
      diContainer.imageService
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = layoutInflater.inflate(layout.activity_main, null) as CatsView
    setContentView(view)

    /**
     * Для проверки ДЗ по "презентеру" нужно раскомментировать ниже логику для презентера
     * и закомментировать логику вьюмодели
     * */
//    catsPresenter = CatsPresenter(diContainer.catsService, diContainer.imageService)
//    view.presenter = catsPresenter
//    catsPresenter.attachView(view)
//    catsPresenter.onInitComplete()

    /**
     * Для проверки ДЗ по "презентеру" нужно закомментировать ниже логику вьюмодели и раскомментировать
     * логику презентера выше
     * */
    vm.resultLiveData.observe(this) { catsResult ->
      when (catsResult) {
        Loading -> view.showLoading()
        is Failure -> {
          view.hideLoading()
          view.respondOnError(catsResult.throwable?.message ?: catsResult.additionalMessage)
        }
        is Success -> {
          view.hideLoading()
          view.populate(catsResult.factVO)
        }
      }
    }

    view.button.setOnClickListener { vm.load() }
  }

  override fun onStop() {
    if (isFinishing) {
      catsPresenter.detachView()
    }
    super.onStop()
  }
}