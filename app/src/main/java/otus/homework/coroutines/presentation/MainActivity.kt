package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.R
import otus.homework.coroutines.models.Cat
import otus.homework.coroutines.models.Result
import otus.homework.coroutines.presentation.vm.CatsViewModel
import otus.homework.coroutines.presentation.vm.CatsViewModelFactory

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(
            diContainer.catFactsService,
            diContainer.catImagesService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.setOnButtonClick {
            catsViewModel.getCat()
        }

        val catObserver = Observer<Result<Cat>> { catResult ->
            when(catResult) {
                is Result.Success -> view.populate(catResult.data!!)
                is Result.Error -> view.showErrorToast(catResult.error!!)
            }

        }
        catsViewModel.cat.observe(this, catObserver)
    }

    override fun onStart() {
        super.onStart()
        catsViewModel.getCat()
    }

    override fun onStop() {
        super.onStop()
    }
}