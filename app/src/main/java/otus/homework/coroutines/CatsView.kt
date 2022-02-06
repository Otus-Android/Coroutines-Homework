package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var viewModel: CatsViewmodel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            //presenter?.onInitComplete()
            viewModel?.getData()
        }
    }

    override fun populate(fact: FactAndImage) {
        val textField = findViewById<TextView>(R.id.fact_textView)
        val imageView = findViewById<ImageView>(R.id.cats_image)

        textField.text = fact.fact.text

        Picasso
            .get()
            .load(fact.image.link)
            .into(imageView)
    }

    private fun initObservers() {
        viewModel?.data?.observe(context as AppCompatActivity, {
            when (it) {
                is Result.Success -> {
                    populate(it.data)
                }
                is Result.Error -> {
                    it.msg?.let{
                        onError(it)
                    }
                }
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initObservers()
    }

    override fun onError(errorText: String) {
        Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(fact: FactAndImage)

    fun onError(errorText: String)
}