package otus.homework.coroutines.data

import otus.homework.coroutines.domain.CatRandomFact

fun convertToCatRandomFact(fact: Fact?, image: Image?): CatRandomFact {
    return CatRandomFact(fact?.text, image?.url)
}