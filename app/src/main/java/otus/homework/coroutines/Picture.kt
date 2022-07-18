package otus.homework.coroutines

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




data class Picture (
    @SerializedName("file")
    @Expose
    val file: String? = null
    )