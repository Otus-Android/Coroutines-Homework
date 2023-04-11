package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var catsViewModel :CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel?.onInitComplete()
        }
    }


    override fun populate(data: Data) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact
        Picasso.get()
            .load(data.image)
            .into(findViewById<ImageView>(R.id.imageIV))
    }
}

interface ICatsView {

    fun populate(data: Data)
}