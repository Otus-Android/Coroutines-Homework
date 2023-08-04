package otus.homework.coroutines.presentation.mvi.components

import android.content.Context
import android.widget.Toast
import io.reactivex.functions.Consumer
import otus.homework.coroutines.presentation.mvi.models.News

/**
 * Обработчик новостей [News]
 *
 * @param context `application context`
 */
class NewsConsumer(private val context: Context) : Consumer<News> {

    override fun accept(news: News) =
        when (news) {
            is News.ErrorLoadingCat -> show(news)
        }

    private fun show(news: News.ErrorLoadingCat) =
        Toast.makeText(context, news.message, Toast.LENGTH_SHORT).show()
}