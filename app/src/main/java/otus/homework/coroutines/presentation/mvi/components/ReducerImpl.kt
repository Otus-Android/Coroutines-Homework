package otus.homework.coroutines.presentation.mvi.components

import com.badoo.mvicore.element.Reducer
import otus.homework.coroutines.presentation.mvi.models.Effect
import otus.homework.coroutines.presentation.mvi.models.State

/**
 * Производитель новых состояний [State] на основе текущего значения и внутреннего изменения [Effect]
 */
class ReducerImpl : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State =
        when (effect) {
            Effect.StartedLoading -> State.Idle
            is Effect.SuccessfulLoaded -> State.Success(effect.cat)
            is Effect.ErrorLoading -> State.Idle
        }
}