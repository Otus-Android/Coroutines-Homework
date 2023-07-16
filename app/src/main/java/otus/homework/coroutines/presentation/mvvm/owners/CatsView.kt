package otus.homework.coroutines.presentation.mvvm.owners

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.models.presentation.CatUiState
import otus.homework.coroutines.presentation.mvvm.CatsViewModel
import otus.homework.coroutines.utils.CustomApplication

/**
 * `Custom view` и информацией о случайном коте.
 *
 * Построено на основе использования [ViewModel] и `custom` [ViewModelStoreOwner], [LifecycleOwner].
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
                viewModel.uiState.collect { state -> update(state) }
            }
        }
    }

    private fun update(state: CatUiState) {
        when (state) {
            is CatUiState.Idle -> onIdle()
            is CatUiState.Success -> onSuccess(state)
            is CatUiState.Error -> onError(state)
        }
    }

    private fun onIdle() {
        imageView.setImageDrawable(null)
    }

    private fun onSuccess(state: CatUiState.Success) {
        textView.text = state.cat.fact
        picasso.load(state.cat.image).placeholder(R.drawable.question_mark).into(imageView)
    }

    private fun onError(state: CatUiState.Error) {
        if (!state.isShown) {
            val toast = Toast.makeText(context, state.message, Toast.LENGTH_SHORT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                toast.addCallback(object : Toast.Callback() {
                    override fun onToastHidden() {
                        viewModel.onErrorShown()
                        toast.removeCallback(this)
                    }
                })
                toast.show()
            } else {
                toast.show()
                viewModel.onErrorShown()
            }
        }
    }

    override fun onDetachedFromWindow() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        viewModelStore.clear()
        super.onDetachedFromWindow()
    }

    private fun initViewModel(): CatsViewModel = ViewModelProvider(
        this, CustomApplication.diContainer(context).catsViewModelFactory
    )[CatsViewModel::class.java]

    override fun getViewModelStore() = viewModelStore

    override fun getLifecycle() = lifecycleRegistry
}