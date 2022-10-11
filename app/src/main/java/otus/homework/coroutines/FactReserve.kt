package otus.homework.coroutines

data class FactReserve(val fact: String, val length: Int) {
    fun toFact(): Fact = Fact("", false, "", this.fact, "", false, "", "", "")
}
