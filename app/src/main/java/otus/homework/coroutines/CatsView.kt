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

    private var onButtonClick: (() -> Unit)? = null

    fun setOnButtonClick(listener: () -> Unit) {
        onButtonClick = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onButtonClick?.invoke()
        }
    }

    override fun populate(meme: Meme) {
        findViewById<TextView>(R.id.tv_meme_caption).text = meme.caption
        val imgView = findViewById<ImageView>(R.id.img_meme)
        Picasso.get().load(meme.image).into(imgView)
    }

    override fun socketExceptionMessage() {
        Toast.makeText(context, context.getString(R.string.no_server_response), Toast.LENGTH_SHORT)
            .show()
    }

    override fun baseExceptionMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(meme: Meme)

    fun socketExceptionMessage()

    fun baseExceptionMessage(msg: String)
}