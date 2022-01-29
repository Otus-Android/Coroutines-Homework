package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import otus.homework.coroutines.CatsPresenter
import otus.homework.coroutines.R

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

    override fun populate(factAndPicture: FactAndPicture) {
        findViewById<TextView>(R.id.fact_textView).text = factAndPicture.fact.text

        Picasso.get()
            .load(factAndPicture.picture.file)
            .into(findViewById<ShapeableImageView>(R.id.picture_shapeableImageView))
    }

    override fun showError(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun showError(message: String)
    fun populate(factAndPicture: FactAndPicture)
}