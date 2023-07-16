package otus.homework.coroutines.presentation.mvvm

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
import otus.homework.coroutines.utils.CustomApplication

/**
 * `Custom view` и информацией о случайном коте.
 *
 * Построено на основе использования [ViewModel].
 * Отличительная особенность: используются данные ближайших компонентов вверх по иерархии для
 * получения [ViewModelStoreOwner] и [LifecycleOwner], на основе которых и производится обработка
 * данныхю.
 */
class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val picasso: Picasso = CustomApplication.diContainer(context).picasso

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    private val viewModel: CatsViewModel by lazy(LazyThreadSafetyMode.NONE) { initViewModel() }

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
        viewModel.getRandomCat(false)
        initObservers()
    }

    private fun initObservers() {
        findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
            lifecycleOwner.lifecycleScope.launch {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect { state -> update(state) }
                }
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
        picasso
            .load(state.cat.image)
            .placeholder(R.drawable.question_mark)
            .into(imageView)
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

    private fun initViewModel(): CatsViewModel =
        findViewTreeViewModelStoreOwner()?.let { viewModelStoreOwner ->
            ViewModelProvider(
                viewModelStoreOwner,
                CustomApplication.diContainer(context).catsViewModelFactory
            )[CatsViewModel::class.java]
        } ?: throw IllegalStateException()
}