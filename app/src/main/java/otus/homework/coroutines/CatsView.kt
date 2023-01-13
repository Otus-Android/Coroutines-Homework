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
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(vo: CatsVO) {
        findViewById<TextView>(R.id.fact_textView).text = vo.fact
        val meowImageView = findViewById<ImageView>(R.id.meow_imageView)
        Picasso.get().load(vo.imageUrl).into(meowImageView)
    }

    override fun showToast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun setButtonClickListener(action: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            action.invoke()
        }
    }
}

interface ICatsView {

    fun populate(vo: CatsVO)

    fun showToast(text: String?)
}