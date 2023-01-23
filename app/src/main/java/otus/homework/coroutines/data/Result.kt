package otus.homework.coroutines.data

sealed class Result(val exception:Throwable?){
    class Success(val fact:String, val image:String): Result(null)
    class Error(t:Throwable): Result(t)
}

