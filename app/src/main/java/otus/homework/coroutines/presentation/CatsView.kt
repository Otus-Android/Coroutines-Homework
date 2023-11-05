package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.di.components.MainActivityComponent
import otus.homework.coroutines.presentation.utlis.ViewModelFactory
import otus.homework.coroutines.presentation.utlis.activityComponent
import otus.homework.coroutines.presentation.utlis.setupOnStopListener
import javax.inject.Inject

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private val viewModel by lazy {
        findViewTreeViewModelStoreOwner()?.let { storeOwner ->
            ViewModelProvider(storeOwner, viewModelFactory)[CatsViewModel::class.java]
        }
    }

    private var refreshButton: Button? = null
    private var progressBar: ProgressBar? = null
    private var textView: TextView? = null
    private var photo: ImageView? = null

    override fun onAttachedToWindow() {
        context.activityComponent<MainActivityComponent>().inject(this)
        super.onAttachedToWindow()
        setupChildren()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel?.let { viewModel ->
            setupOnStopListener(viewModel::refreshFact, viewModel::stopWorking)
            findViewTreeLifecycleOwner()?.let { viewModel.state.observe(it, ::populate) }
        }
    }

    private fun populate(state: ScreenState) {
        when(state) {
            is ScreenState.Error -> showError(state.message)
            is ScreenState.Loading -> showLoading(true)
            is ScreenState.Model -> showContent(state)
            is ScreenState.TimeoutException -> showError(context.getString(R.string.timeout_exception))
            is ScreenState.Empty -> hideAllChildren()
        }
    } 

    private fun setupChildren() {
        textView = findViewById(R.id.fact_textView)
        progressBar = findViewById(R.id.loading_view)
        photo = findViewById(R.id.photo)
        refreshButton = findViewById<Button?>(R.id.button).apply {
            setOnClickListener { viewModel?.refreshFact() }
        }
    }

    private fun showError(message: String?) {
        val text = if (message.isNullOrBlank()) {
            context.getString(R.string.default_exception)
        } else {
            message
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        progressBar?.isVisible = state
        textView?.isVisible = !state
        photo?.isVisible = !state
        refreshButton?.isEnabled = !state
    }

    private fun hideAllChildren() {
        progressBar?.isVisible = false
        textView?.isVisible = true
        photo?.isVisible = true
        refreshButton?.isEnabled = true
    }

    private fun showContent(model: ScreenState.Model) {
        textView?.text = model.text
        picasso
            .load(model.photoUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .into(photo)
        showLoading(false)
    }
}