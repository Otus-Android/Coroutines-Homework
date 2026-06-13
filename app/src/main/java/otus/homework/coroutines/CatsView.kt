package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
    }

    fun populate(presentationModel: PresentationModel) {
        findViewById<TextView>(R.id.fact_textView).text = presentationModel.fact
        Picasso
            .get()
            .load(presentationModel.imageUrl)
            .into(findViewById<ImageView>(R.id.cat_imageView))
    }

    fun showToast(message: String) {
        Toast
            .makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            )
            .show()
    }
}

interface ICatsView {

    fun populate(presentationModel: PresentationModel)

    fun showToast(message: String)
}