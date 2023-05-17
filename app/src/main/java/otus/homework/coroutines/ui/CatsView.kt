package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatInfo

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(catInfo: CatInfo) {
        findViewById<TextView>(R.id.fact_textView).text = catInfo.fact
        val imageView = findViewById<ImageView>(R.id.image)
        Picasso.get()
            .load(catInfo.pictureUrl)
            .error(R.drawable.ic_load_error_vector)
            .into(imageView)
    }

    override fun showTimeoutMessage() {
        Toast.makeText(context, resources.getString(R.string.socket_timeout_text), Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String?) {
        CrashMonitor.trackWarning()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun addOnClickListener(callback: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            callback.invoke()
        }
    }
}

interface ICatsView {

    fun populate(catInfo: CatInfo)
    fun showTimeoutMessage()
    fun showMessage(message: String?)
}
