package otus.homework.coroutines.presentation.mvvm.owners

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.presentation.mvvm.owners.models.CatUiState
import otus.homework.coroutines.utils.CustomApplication

/**
 * `Custom view` с информацией о случайном коте.
 *
 * Построено на основе паттерна `MVVM` с использованием [CatsViewModel],
 * `custom` [ViewModelStoreOwner] и [LifecycleOwner].
 * Отличительная особенность: обработка данных производится в рамках своего жизненного цикла.
 */
class CatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ViewModelStoreOwner, LifecycleOwner {

    private val viewModelStore: ViewModelStore = ViewModelStore()
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    private val viewModel: CatsViewModel by lazy(LazyThreadSafetyMode.NONE) { initViewModel() }

    private val picasso: Picasso = CustomApplication.diContainer(context).picasso

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        textView = findViewById(R.id.fact_text_view)
        imageView = findViewById(R.id.image_view)
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.getRandomCat(true)
            picasso.cancelRequest(imageView)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        viewModel.getRandomCat(false)
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.uiState.collect { state -> update(state) }
                }
                launch(Dispatchers.Main.immediate) {
                    viewModel.errorEvents.collect { message -> onError(message) }
                }
            }
        }
    }

    private fun update(state: CatUiState) {
        when (state) {
            is CatUiState.Idle -> onIdle()
            is CatUiState.Success -> onSuccess(state)
        }
    }

    private fun onIdle() {
        imageView.setImageDrawable(null)
    }

    private fun onSuccess(state: CatUiState.Success) {
        textView.text = state.cat.fact
        picasso.load(state.cat.image).placeholder(R.drawable.question_mark).into(imageView)
    }

    private fun onError(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    override fun onDetachedFromWindow() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        viewModelStore.clear()
        super.onDetachedFromWindow()
    }

    private fun initViewModel(): CatsViewModel = ViewModelProvider(
        this, with(CustomApplication.diContainer(context)) {
            CatsViewModel.provideFactory(catRepository, stringProvider, dispatcher)
        }
    )[CatsViewModel::class.java]

    override fun getViewModelStore() = viewModelStore

    override fun getLifecycle() = lifecycleRegistry
}