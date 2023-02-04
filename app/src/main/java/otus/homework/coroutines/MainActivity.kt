package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    lateinit var catsViewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel = CatsViewModel(diContainer.service, diContainer.catPicService)
        catsViewModel.loadData()

        catsViewModel.catsViewState.observe(this){result ->
            when(result){
                is Result.Success -> view.populate(result.item)
                is Result.Error -> view.showToast(result.message)
            }
        }
        findViewById<Button>(R.id.moreFacts_button).apply{
            setOnClickListener { catsViewModel.loadData() }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsViewModel.detachView()
        }
        super.onStop()
    }
}