package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
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

    var myViewModel :MyViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            myViewModel?.onInitComplete()
        }
    }

    override fun populate(factAndPicture: FactAndPicture) {
        findViewById<TextView>(R.id.fact_textView).text = factAndPicture.fact
        val ivBasicImage: ImageView = findViewById<ImageView>(R.id.img);
        val imageUri = factAndPicture.urlPicture
        Picasso.get().load(imageUri).into(ivBasicImage)
        findViewById<Button>(R.id.button).isEnabled = true
    }
}

interface ICatsView {
    fun populate(factAndPicture: FactAndPicture)

}