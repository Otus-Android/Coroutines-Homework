package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import otus.homework.coroutines.api.Result
import otus.homework.coroutines.utils.showToast

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CatsViewModel> { ViewModelFactory(diContainer.catsRemoteDateSource) }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener { viewModel.onInitComplete() }
        viewModel.onInitComplete()
        viewModel.catInfo.observe(this, ::handleCatInfoResult)
        viewModel.showServerError.observe(this, ::showServerError)
    }

    private fun handleCatInfoResult(catInfo: Result<CatInfo>) {
        when (catInfo) {
            is Result.Success -> populate(catInfo.data)
            is Result.Error -> showToast(catInfo.exception.message.orEmpty())
        }
    }

    private fun populate(catInfo: CatInfo) {
        findViewById<TextView>(R.id.fact_textView).text = catInfo.fact.text
        Picasso.get().load(catInfo.imageUrl).into(findViewById<ImageView>(R.id.image))
    }

    private fun showServerError(show: Boolean) {
        if (show) showToast(R.string.error_text_no_network)
    }
}