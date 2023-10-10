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

    override fun populate(meowInfo: MeowInfo) {
        findViewById<TextView>(R.id.fact_textView).text = meowInfo.fact

        val imageView = findViewById<ImageView>(R.id.pic)
        Picasso.get()
            .load(meowInfo.pic)
            .placeholder(R.drawable.ic_image_placeholder_128)
            .error(R.drawable.ic_question_mark_128)
            .into(imageView)
    }

    override fun showToast(msg: String?) {
        Toast.makeText(context, msg ?: "Error", Toast.LENGTH_SHORT).show()
    }

    override fun setClickListener(block: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            block.invoke()
        }
    }
}

interface ICatsView {
    fun populate(meowInfo: MeowInfo)
    fun showToast(msg: String?)
    fun setClickListener(block: () -> Unit)
}