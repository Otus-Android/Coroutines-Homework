package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.CatFactPic
import otus.homework.coroutines.viewmodel.CatsViewModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private var catsViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                catsViewModel?.onInitComplete()
            }
        }
    }

    override fun populate(catfactpic: CatFactPic) {
        findViewById<TextView>(R.id.fact_textView).text = catfactpic.fact
        findViewById<ImageView>(R.id.cat_image).setImageBitmap(catfactpic.picUrl)
    }

    override fun showToast(message:String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun populate(catfactpic: CatFactPic)
    fun showToast(message:String)
}