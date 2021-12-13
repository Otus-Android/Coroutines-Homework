package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.bumptech.glide.request.RequestOptions




class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private fun setButtonEnabled(){
        findViewById<Button>(R.id.button).isEnabled = true
    }

    override fun showToastMsg(id: Int) {
        setButtonEnabled()
        Toast.makeText(context, id, Toast.LENGTH_LONG).show()
    }

    override fun showToastMsg(msg: String) {
        setButtonEnabled()
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun populate(fact: FactAndPicture) {
        setButtonEnabled()
        findViewById<TextView>(R.id.fact_textView).text = fact.fact.text
        Glide.with(context)
            .load(fact.picture.file)
            .apply(RequestOptions().fitCenter())
            .into(findViewById(R.id.cat_image))
    }

}

interface ICatsView {
    fun showToastMsg(id: Int)
    fun showToastMsg(msg: String)
    fun populate(fact: FactAndPicture)
}