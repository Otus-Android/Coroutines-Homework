package otus.homework.coroutines

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

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var diContainer: DiContainer? = null
    private val viewModel: CatsViewModel by lazy {
        ViewModelProvider(
            findViewTreeViewModelStoreOwner()!!,
            diContainer?.catsViewModelProvider!!
        )[CatsViewModel::class.java]
    }
    private val textView: TextView by lazy { findViewById(R.id.fact_textView) }
    private val imageView: ImageView by lazy { findViewById(R.id.image) }
    private val button: Button by lazy { findViewById(R.id.button) }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initObservers()
        button.setOnClickListener { viewModel.onInitComplete() }
        viewModel.onInitComplete()
    }

    private fun initObservers() {
        viewModel.state.observe(findViewTreeLifecycleOwner()!!) {
            when (it) {
                is Success<CatsViewState> -> populate(it.state)
                is Error -> showToast(it.message)
            }
        }
    }

    override fun populate(state: CatsViewState) {
        textView.text = state.fact.text
        diContainer?.picasso
            ?.load(state.imageUrl)
            ?.into(imageView)
    }

    override fun showToast(message: String) =
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

interface ICatsView {

    fun populate(state: CatsViewState)

    fun showToast(message: String)
}