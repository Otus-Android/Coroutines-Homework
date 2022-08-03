package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import retrofit2.Response.error

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

//    var presenter :CatsPresenter? = null
//
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
//        }
//    }

    override fun populate(catsViewState: CatsViewState) {
        findViewById<TextView>(R.id.fact_textView).text = catsViewState.fact.text
        val imageView = findViewById<ImageView>(R.id.cats_photo)

        Picasso.get()
            .load(catsViewState.photo.file)
            .resize(200, 200)
            .into(imageView)
    }

    override fun showToast(text : String?){
        val outText = text ?: "Не удалось получить ответ от сервером"
        Toast.makeText(
            context,
            outText,
            Toast.LENGTH_SHORT
        ).show()
    }
}

interface ICatsView {
    fun populate(catsViewState: CatsViewState)
    fun showToast(text : String?)
}