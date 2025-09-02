package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import androidx.constraintlayout.widget.ConstraintLayout


class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

//    var presenter: CatsPresenter? = null

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
        Picasso.get().load(cat.imageUrl).into(findViewById<ImageView>(R.id.catImageView));
    }

    override fun onError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showErrorToast(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    fun setOnButtonClick(onButtonClick: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            Log.d("TAG", "Button clicked")
            onButtonClick()
        }
    }

}

    public interface ICatsView {

        fun populate(cat: Cat)

        fun onError(errorMsg: String)
    }

