package otus.homework.coroutines

fun mapResponse(fact: FactResponse, pictureUrl: String) =
        CatViewItem(
            fact = fact.text,
            pictureUrl = pictureUrl
        )
