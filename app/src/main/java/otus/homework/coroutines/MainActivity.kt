package otus.homework.coroutines

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import otus.homework.coroutines.presenter.CatsPresenter
import otus.homework.coroutines.utils.CatsService
import otus.homework.coroutines.utils.DiContainer
import otus.homework.coroutines.view.CatsView
import otus.homework.coroutines.viewmodel.CatsViewModel

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    lateinit var catsViewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        /* MVP */

//        catsPresenter = CatsPresenter(diContainer.catFactService, diContainer.catImageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        /* MVVM */

        catsViewModel = ViewModelProvider(
            this,
            CatsViewModelFactory(
                application,
                diContainer,
            )
        ).get(CatsViewModel::class.java)

        catsViewModel.catDataResponse.observe(this) {
            when (it) {
                is CatsViewModel.Result.Success -> {
                    fact_textView.text = it.data.fact.firstOrNull()?.fact
                    Picasso.get().load(it.data.image.fileUrl)
                        .into(image_imageView)
                }
                is CatsViewModel.Result.Error -> {
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    class CatsViewModelFactory(
        application: Application,
        private val diContainer: DiContainer,
    ) : ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CatsViewModel(diContainer) as T
        }
    }
}