package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.presentation.entitiy.CatFact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(fact: CatFact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.funFact
        runCatching {
            Picasso.get()
                .load(fact.photoUri)
                .into(
                    findViewById<ImageView>(R.id.fact_imageView)
                )
        }
    }

    override fun postWarning(messageProvider: Context.() -> String) {
        post {
            Toast
                .makeText(context, messageProvider.invoke(context), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun attachOnButtonClickCallback(callback: ICatsView.() -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener { callback() }
    }
}

interface ICatsView {

    fun populate(fact: CatFact)
    fun postWarning(messageProvider: Context.() -> String)
    fun attachOnButtonClickCallback(callback: ICatsView.() -> Unit)
}