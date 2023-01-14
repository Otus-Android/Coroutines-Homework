package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatFactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  ViewModelProvider(this)[CatFactViewModel::class.java]
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        viewModel.onInitComplete()
        view.viewModel = viewModel
        subscribeEvent(view)
    }

    private fun subscribeEvent(view: CatsView){
        viewModel.catFact.observe(this){
            view.populate(it)
        }

        viewModel.error.observe(this){
            view.showErrorMessage(it)
        }
    }
}