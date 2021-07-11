package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
    @field:SerializedName("fact") var fact: String
)