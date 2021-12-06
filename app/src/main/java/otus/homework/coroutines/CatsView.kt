package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.model.CatModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {
    //var presenter :CatsPresenter? = null
    var clickAction: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            //presenter?.onInitComplete()
            clickAction?.invoke()
        }
    }

    override fun populate(model: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact.text
        var imageView = findViewById<ImageView>(R.id.fact_imageView)
        Picasso.get()
            .load(model.image.file)
            .into(imageView);
    }

    override fun toast(text:String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(model: CatModel)
    fun toast(text:String)
}