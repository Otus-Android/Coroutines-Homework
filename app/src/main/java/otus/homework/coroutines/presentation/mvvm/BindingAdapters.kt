package otus.homework.coroutines.presentation.mvvm

import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun ImageView.setImage(imageUrl: String?) {
    imageUrl?.let {
        Picasso.get()
            .load(imageUrl)
            .into(this)
    }
}

@BindingAdapter("scrollable")
fun TextView.setScrollable(isScrollable: Boolean?) {
    isScrollable?.takeIf { isScrollable }?.let { movementMethod = ScrollingMovementMethod() }
}