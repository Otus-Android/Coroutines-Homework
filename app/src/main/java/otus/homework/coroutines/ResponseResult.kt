package otus.homework.coroutines

sealed interface ResponseResult{
    class Success(val catModal: CatModal) : ResponseResult
    class Error(val throwable: Throwable) : ResponseResult
}