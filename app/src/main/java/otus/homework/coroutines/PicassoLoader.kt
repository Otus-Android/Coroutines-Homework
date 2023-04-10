package otus.homework.coroutines

import android.widget.ImageView
import com.squareup.picasso.Picasso

class PicassoLoader(
    private val loader: Picasso
): ImageLoader {

    override fun load(url: String, into: ImageView) {
        loader.load(url)
            .placeholder(R.drawable.baseline_image_24)
            .into(into)
    }
}