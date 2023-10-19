package otus.homework.coroutines.presentation.model

data class PictureModel(val url : String){
    companion object{
        fun getDefault() = PictureModel("")
    }
}