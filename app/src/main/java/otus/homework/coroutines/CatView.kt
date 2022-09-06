package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatView {

    //    var presenter: CatsPresenter? = null
    var viewModel: CatViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(factAndImage: FactAndImage) {
        findViewById<TextView>(R.id.fact_textView).text = factAndImage.fact
        Picasso.get().load(factAndImage.image).into(findViewById<ImageView>(R.id.fact_imageView))
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatView {
    fun populate(factAndImage: FactAndImage)
    fun showToastMessage(message: String)
}