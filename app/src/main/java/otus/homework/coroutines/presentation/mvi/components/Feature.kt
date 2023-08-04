package otus.homework.coroutines.presentation.mvi.components

import com.badoo.mvicore.feature.ActorReducerFeature
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.presentation.mvi.models.Effect
import otus.homework.coroutines.presentation.mvi.models.News
import otus.homework.coroutines.presentation.mvi.models.State
import otus.homework.coroutines.presentation.mvi.models.Wish
import otus.homework.coroutines.utils.StringProvider

/**
 * Конфигурация логики обработки компонентов по загрузке кота
 *
 * @param repository репозиторий информации о кошке
 * @param stringProvider поставщик строковых значений
 */
class Feature(
    repository: CatRepository,
    stringProvider: StringProvider
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State.Idle,
    actor = ActorImpl(repository, stringProvider),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
)