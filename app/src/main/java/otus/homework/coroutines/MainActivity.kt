package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val catsPresenterModel: CatsViewModel by lazy { ViewModelProvider(this)[CatsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.setOnReloadAction(catsPresenterModel::doReadThings)

        catsPresenterModel.giveMeCatsData {
            view.populateCatsData(it)
        }
    }
}
