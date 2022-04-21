package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.CatDTO
import otus.homework.coroutines.models.Fact

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

    override fun populate(catFactWithPhoto: CatDTO) {
        Picasso.get()
            .load(catFactWithPhoto.photo.photoUrl)
            .into(findViewById<ImageView>(R.id.cat_photo))

        findViewById<TextView>(R.id.fact_textView).text = catFactWithPhoto.fact.text
    }

    override fun showServerResponseError() {
        Toast.makeText(context, R.string.failed_to_get_response_from_server, Toast.LENGTH_SHORT).show()
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catFactWithPhoto: CatDTO)
    fun showServerResponseError()
    fun showError(errorMessage: String)
}