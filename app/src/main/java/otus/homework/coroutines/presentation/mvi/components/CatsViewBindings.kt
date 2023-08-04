package otus.homework.coroutines.presentation.mvi.components

import androidx.lifecycle.LifecycleOwner
import com.badoo.mvicore.android.AndroidBindings
import otus.homework.coroutines.presentation.mvi.CatsView
import otus.homework.coroutines.presentation.mvi.models.News


/**
 * Связыватель компонентов по загрузке кота
 *
 * @param lifecycleOwner владелец жизненного цикла
 * @param feature конфигурация логики обработки компонентов по загрузке кота
 * @param newsConsumer абработчик новостей [News]
 */
class CatsViewBindings(
    lifecycleOwner: LifecycleOwner,
    private val feature: Feature,
    private val newsConsumer: NewsConsumer
) : AndroidBindings<CatsView>(lifecycleOwner) {

    override fun setup(view: CatsView) {
        binder.bind(feature to view)
        binder.bind(feature.news to newsConsumer)
    }
}
