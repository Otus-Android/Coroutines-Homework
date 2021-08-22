package otus.homework.coroutines.view

import android.app.Application
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.R
import otus.homework.coroutines.utils.DiContainer
import otus.homework.coroutines.viewmodel.CatsViewModel

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    lateinit var catsViewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)

        catsViewModel = ViewModelProvider(
            this,
            CatsViewModelFactory(application, diContainer)
        ).get(CatsViewModel::class.java)

        initObserver()
        initListeners()

    }

    private fun initListeners() {
        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.onInitComplete()
        }
    }

    private fun initObserver() {
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

    class CatsViewModelFactory(
        application: Application,
        private val diContainer: DiContainer,
    ) : ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CatsViewModel(diContainer) as T
        }
    }
}