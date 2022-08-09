package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.CatsInfo

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var onClick: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onClick?.invoke()
        }
    }

    override fun handleNewState(result: Result<CatsInfo>) {
        when (result) {
            is Error -> displayErrorMessage(result.message)
            is Success -> populate(result.model)
        }
    }

    private fun populate(model: CatsInfo) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact
        findViewById<ImageView>(R.id.cat_imageView).also {
            Picasso.get()
                .load(model.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(it)
        }
    }

    private fun displayErrorMessage(message: String?) {
        val errorMessage = message ?: resources.getString(R.string.default_error_message)
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun handleNewState(result: Result<CatsInfo>)
}