package id.adriesavana.medicineinventory.domain

import id.adriesavana.medicineinventory.utils.BaseUseCase
import id.adriesavana.medicineinventory.utils.DataResult
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.utils.ActionResult
import kotlinx.coroutines.CoroutineDispatcher

class RegisterUseCase(
    private val repository: Repository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<RegisterUseCase.Params, String, RegisterActionResult>(
    dispatcher
) {
    override suspend fun execute(param: Params?): DataResult<String> {
        require(param != null) { "please added param" }
        val registerResult = repository.register(param.email, param.password)
        if (registerResult is DataResult.Exception) {
            return registerResult
        }

        return repository.signIn(param.email, param.password)
    }

    override suspend fun DataResult<String>.transformToUseCaseResult(): RegisterActionResult =
        when (this) {
            is DataResult.Success -> RegisterActionResult.Success
            is DataResult.Exception -> RegisterActionResult.Failed
        }

    data class Params(val email: String, val password: String)
}

sealed class RegisterActionResult : ActionResult {
    object Success : RegisterActionResult()
    object Failed : RegisterActionResult()
}
