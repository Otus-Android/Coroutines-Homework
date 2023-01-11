package otus.homework.coroutines.ui.model

sealed class CatsResult {
  class Success(val factVO: FactVO): CatsResult()
  object Loading : CatsResult()
  class Failure(val throwable: Throwable?, val additionalMessage: String) : CatsResult()
}
