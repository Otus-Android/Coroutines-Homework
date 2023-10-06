package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.presentation.CatContent

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(content: CatContent) {
        findViewById<TextView>(R.id.fact_textView).text = content.fact
        Picasso.get()
            .load(content.image)
            .resize(250, 250)
            .centerCrop()
            .into(findViewById<ImageView>(R.id.iv_cat))
    }

    override fun showToast(text: String) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun showToast(text: String)
    fun populate(content: CatContent)

}