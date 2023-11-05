package otus.homework.coroutines.data.mapper

import otus.homework.coroutines.data.model.FactEntity
import otus.homework.coroutines.domain.model.FactModel
import javax.inject.Inject

class FactMapper @Inject constructor() {

    fun map(entity: FactEntity): FactModel {
        return FactModel(text = entity.text)
    }
}