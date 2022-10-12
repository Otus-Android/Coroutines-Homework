package otus.homework.coroutines

/** for https://catfact.ninja/fact */
data class FactReserve(val fact: String, val length: Int) {
    fun toFact(): Fact = Fact("", false, "", this.fact, "", false, "", "", "")
}
