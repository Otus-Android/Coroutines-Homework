package otus.homework.coroutines

class Mapper {
  fun map(fact: Fact, image: Image): Cat {
    return Cat(fact.text, image.src)
  }
}