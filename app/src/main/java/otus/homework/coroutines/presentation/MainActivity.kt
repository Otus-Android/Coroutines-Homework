package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import otus.homework.coroutines.R
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import otus.homework.coroutines.entity.CatData

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels()

    private var catData: CatData? = null

    private var error: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutInflater.inflate(R.layout.activity_main, null))
        setClickListeners()
        observeOnEvents()
        if (savedInstanceState == null) {
            viewModel.onInitComplete()
        } else {
            catData = savedInstanceState.getParcelable(CAT_DATA_KEY)
            error = savedInstanceState.getString(ERROR_KEY, null)
            catData?.let { populate(it) } ?: error?.let { error(it) }
        }
    }

    private fun observeOnEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ShowPhotoState -> {
                            catData = state.catData
                            catData?.let {
                                populate(it)
                                error = null
                            }
                        }
                        is ErrorState -> {
                            error = state.error
                            error?.let {
                                exceptionToast(it)
                                catData = null
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CAT_DATA_KEY, catData)
        outState.putString(ERROR_KEY, error)
    }

    private fun setClickListeners() {
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
    }

    private fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.factData?.fact
        catData.photoData?.let { photo ->
            viewModel.setUrlPhotoInView(findViewById(R.id.catPhoto), photo.url)
        }
    }

    private fun exceptionToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val CAT_DATA_KEY = "cat_data"
        const val ERROR_KEY = "error_key"
    }
}