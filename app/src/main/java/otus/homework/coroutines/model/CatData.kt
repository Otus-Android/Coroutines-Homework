package otus.homework.coroutines.model

import kotlinx.coroutines.Deferred

data class CatData(
    val fact: Deferred<List<Fact>>,
    val image: Deferred<CatImage>
)