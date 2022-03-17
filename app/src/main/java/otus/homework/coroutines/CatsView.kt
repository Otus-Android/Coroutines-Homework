package otus.homework.coroutines

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import otus.homework.coroutines.model.Fact
import java.net.URL
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.model.Result.*

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populateCatsData(catsData: CatsViewModel.CatsData) {
        catsData?.resultFact?.let { findViewById<TextView>(R.id.fact_textView).text = it }
        catsData?.resultImage?.let {
            findViewById<ImageView>(R.id.imageView).setImageBitmap(it)
        }
        catsData?.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
    }

    override fun setOnReloadAction(action: Runnable) {
        findViewById<Button>(R.id.button).setOnClickListener {
            action.run()
        }
    }
}

interface ICatsView {
    fun populateCatsData(catsData: CatsViewModel.CatsData)
    fun setOnReloadAction(action: Runnable)
}