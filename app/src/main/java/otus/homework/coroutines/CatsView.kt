package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        val scope = PresenterScope()
        findViewById<Button>(R.id.button).setOnClickListener {
            scope.launch {
                presenter?.onInitComplete()
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
    }

    override fun populateImg(catPicture: CatPicture) {
        Picasso.get().load(catPicture.fileUrl).into(findViewById<ImageView>(R.id.img))
    }
}

interface ICatsView {

    fun showToast(message: String)

    fun populate(fact: Fact)

    fun populateImg(catPicture: CatPicture)
}