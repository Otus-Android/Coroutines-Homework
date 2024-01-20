package otus.homework.coroutines

/*
Создать sealed класс Result. Унаследовать от него классы Success<T>, Error.
 Использовать эти классы как стейт необходимый для рендеринга/отображени ошибки
 */
sealed class Result {

    data class Success<T>(val value: T) : Result()

    object Error : Result()

    object Init : Result()

}