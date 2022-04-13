package otus.homework.coroutines.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import otus.homework.coroutines.R
import otus.homework.coroutines.di.CatsViewModelFactory
import otus.homework.coroutines.presentation.CatsState
import otus.homework.coroutines.presentation.CatsViewModel
import otus.homework.coroutines.presentation.ErrorType

class MainActivity : AppCompatActivity() {

    private val mViewModel by viewModels<CatsViewModel>(CatsViewModelFactory::get)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        initButton()
        subscribeToViewModel()
    }

    private fun initButton() {
        findViewById<Button>(R.id.button).setOnClickListener {
            mViewModel.fetch()
        }
    }

    private fun subscribeToViewModel() {
        mViewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)
        mViewModel.event
            .flowWithLifecycle(lifecycle)
            .onEach(::handleEvents)
            .launchIn(lifecycleScope)
    }

    private fun render(state: CatsState) {
        findViewById<TextView>(R.id.fact_textView).text = state.fact?.text ?: ""
        val imageView = findViewById<ImageView>(R.id.image)
        Picasso.get().load(state.image?.url).into(imageView)
    }

    private fun handleEvents(errorType: ErrorType) {
        val message = when (errorType) {
            ErrorType.ServerConnectionError -> getString(R.string.connection_error)
            is ErrorType.OccurredException -> errorType.message
            ErrorType.UnknownError -> return
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}