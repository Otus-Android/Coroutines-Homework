package otus.homework.coroutines

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




data class Picture (
    @SerializedName("file")
    val file: String
    )