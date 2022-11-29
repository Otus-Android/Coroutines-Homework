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

    var presenter :CatsPresenter? = null
    var viewModel :CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            viewModel?.updateData()
        }
    }

    override fun <T>populate(arg: T) {
        when(arg) {
            is Fact -> findViewById<TextView>(R.id.cat_fact_tv).text = arg.fact
            is Image -> {
                val imgView = findViewById<ImageView>(R.id.cat_image_iv)
                Picasso.get()
                    .load(arg.url)
                    .placeholder(android.R.drawable.stat_sys_download)
                    .into(imgView)
            }
            else -> {
                val msg = "Unknown class to populate, only ${Fact::javaClass.name} and ${Image::javaClass.name} allowed"
                throw IllegalArgumentException(msg)
            }
        }
    }

    override fun toast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun <T>populate(arg: T)
    fun toast(message: String)
}