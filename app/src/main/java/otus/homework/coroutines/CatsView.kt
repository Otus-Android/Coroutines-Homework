package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    lateinit var viewModel: CatsViewModel

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
    }

    private fun populate(result: Result.Success<Response>) {
        findViewById<TextView>(R.id.fact_textView).text = result.response.fact
        Picasso.get()
            .load(result.response.image)
            .into(findViewById<ImageView>(R.id.image_view))
    }

    private fun showToast(result: Result.Error) {
        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
    }

    override fun observe(lifecycleOwner: LifecycleOwner) {
        viewModel.catLiveData.observe(lifecycleOwner) { value -> populate(value) }
        viewModel.toastMessage.observe(lifecycleOwner) { value -> showToast(value) }
    }
}

interface ICatsView {
    fun observe(lifecycleOwner: LifecycleOwner)
}