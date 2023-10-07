package otus.homework.coroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel: CatsViewModel by lazy {
        ViewModelProvider(
            this,
            CatsViewModel.getViewModelFactory(
                diContainer.factsRepository,
                diContainer.pictureRepository
            )
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view =  layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.screenState.observe(this){screenState ->
            when (screenState){
                is ScreenState.ShowContent -> view.populate(screenState.content)
                is ScreenState.Error -> view.showToast(screenState.message)
            }
        }
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.getFactsByCoroutines()
        }
    }


    override fun onStop() {
        if (isFinishing) {
        }
        super.onStop()
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}