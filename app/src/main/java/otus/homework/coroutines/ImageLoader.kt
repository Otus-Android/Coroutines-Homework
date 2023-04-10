package otus.homework.coroutines

import android.widget.ImageView

interface ImageLoader {

    fun load(url: String, into: ImageView)
}