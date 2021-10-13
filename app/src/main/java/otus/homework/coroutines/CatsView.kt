package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.ImageFact
import otus.homework.coroutines.models.PresentModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()

        }
    }

    override fun populate(presentModel: PresentModel) {
        findViewById<TextView>(R.id.fact_textView).text = presentModel.fact.text
        Picasso.get()
            .load(presentModel.image.file)
            .into(findViewById<ImageView>(R.id.imageView) )

    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


}



interface ICatsView {
    fun populate(presentModel: PresentModel)
    fun showToast(message: String?)
}