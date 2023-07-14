package otus.homework.coroutines.presentation.viewmodel

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.models.Cat
import otus.homework.coroutines.models.CatState
import otus.homework.coroutines.utils.CustomApplication

class CatsViewWithViewModel @JvmOverloads constructor(
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
            viewModel.getRandomCat()
            picasso.cancelRequest(imageView)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initObservers()
        viewModel.getRandomCat()
    }

    private fun initObservers() {
        findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
            viewModel.resultLiveData.observe(lifecycleOwner) { result ->
                when (result) {
                    is CatState.Idle -> onIdle()
                    is CatState.Success -> onSuccess(result.cat)
                    is CatState.Error -> onError(result.message)
                }
            }
        }
    }

    private fun onIdle() {
        imageView.setImageDrawable(null)
    }

    private fun onSuccess(cat: Cat) {
        textView.text = cat.fact
        picasso
            .load(cat.image)
            .placeholder(R.drawable.question_mark)
            .into(imageView)
    }

    private fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel(): CatsViewModel =
        findViewTreeViewModelStoreOwner()?.let { viewModelStoreOwner ->
            ViewModelProvider(
                viewModelStoreOwner,
                CustomApplication.diContainer(context).catsViewModelFactory
            )[CatsViewModel::class.java]
        } ?: throw IllegalStateException()
}