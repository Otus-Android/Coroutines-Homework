package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact, photo: Photo) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso.get().load(photo.file).into(findViewById<ImageView>(R.id.imageView))
    }

    override fun showMessage(message: Message) {
        Toast.makeText(context, message.stringId, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact, photo: Photo)

    fun showMessage(message: Message)
}