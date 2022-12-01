package otus.homework.coroutines.presentation.mvvm

import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import otus.homework.coroutines.presentation.CatsDataUI

interface UiMapper<T> {
    fun map(obj: T, imageView: ImageView, textView: TextView)
}

class CatsDataUiMapper : UiMapper<CatsDataUI> {
    override fun map(obj: CatsDataUI, imageView: ImageView, textView: TextView) {
        textView.text = obj.fact
        Picasso.get().load(obj.url).into(imageView)
    }
}