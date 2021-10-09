package otus.homework.coroutines.ui.cats

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.data.model.Cat
import otus.homework.coroutines.data.model.Result

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.fetchCats()
        }
    }

    override fun load(result: Result<Cat>) {
        when (result) {
            is Result.Success -> populate(result.data)
            is Result.Error -> connectionError(result.message)
        }
    }

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.text
        val image = findViewById<ImageView>(R.id.cat_image)
        Picasso.get()
            .load(cat.image.file)
            .placeholder(R.drawable.ic_image_black_24dp)
            .into(image)

    }

    override fun connectionError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun load(result: Result<Cat>)

    @Deprecated("Use load instead")
    fun populate(cat: Cat)

    @Deprecated("Use load instead")
    fun connectionError(message: String)
}