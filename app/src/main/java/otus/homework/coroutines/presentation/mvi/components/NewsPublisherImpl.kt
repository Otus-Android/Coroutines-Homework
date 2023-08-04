package otus.homework.coroutines.presentation.mvi.components

import com.badoo.mvicore.element.NewsPublisher
import otus.homework.coroutines.presentation.mvi.models.Effect
import otus.homework.coroutines.presentation.mvi.models.News
import otus.homework.coroutines.presentation.mvi.models.State
import otus.homework.coroutines.presentation.mvi.models.Wish

/**
 * Производитель новостей [News]
 */
class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {

    override fun invoke(wish: Wish, effect: Effect, state: State): News? =
        when (effect) {
            is Effect.ErrorLoading -> News.ErrorLoadingCat(effect.message)
            else -> null
        }
}