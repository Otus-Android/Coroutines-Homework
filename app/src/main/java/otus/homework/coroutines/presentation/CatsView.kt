package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.Cat
import otus.homework.coroutines.R
import otus.homework.coroutines.presentation.vm.CatsViewModel
import otus.homework.coroutines.presentation.vm.CatsViewModelFactory

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    fun setOnButtonClick(onButtonClick: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            Log.d("TAG", "Button clicked")
            onButtonClick()
        }
    }

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
        val imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(cat.imageUrl).into(imageView)
    }

    override fun showErrorToast(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(cat: Cat)

    fun showErrorToast(errorMsg: String)
}