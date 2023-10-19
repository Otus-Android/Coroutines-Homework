package otus.homework.coroutines.presentation.model

data class FactModel(val text: String){
    companion object{
        fun getDefault() = FactModel("")
    }
}