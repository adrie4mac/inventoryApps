package id.adriesavana.medicineinventory.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseUseCase<RequestParam, ResponseObject, Result : ActionResult>
    (private val dispatcher: CoroutineDispatcher) {

    protected abstract suspend fun execute(param: RequestParam?): DataResult<ResponseObject>

    protected abstract suspend fun DataResult<ResponseObject>.transformToUseCaseResult(): Result

    suspend fun getResult(param: RequestParam? = null): Result {
        return withContext(dispatcher) {
            execute(param).transformToUseCaseResult()
        }
    }
}
