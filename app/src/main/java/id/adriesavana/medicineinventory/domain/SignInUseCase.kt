package id.adriesavana.medicineinventory.domain

import id.adriesavana.medicineinventory.utils.BaseUseCase
import id.adriesavana.medicineinventory.utils.DataResult
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.utils.ActionResult
import kotlinx.coroutines.CoroutineDispatcher

class SignInUseCase(
    private val repository: Repository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<SignInUseCase.Params, String, SignInActionResult>(
    dispatcher
) {
    override suspend fun execute(param: Params?): DataResult<String> {
        require(param != null) { "please added param" }
        return repository.signIn(param.email, param.password)
    }

    override suspend fun DataResult<String>.transformToUseCaseResult(): SignInActionResult =
        when (this) {
            is DataResult.Success -> SignInActionResult.Success
            is DataResult.Exception -> SignInActionResult.Failed
        }

    data class Params(val email: String, val password: String)
}

sealed class SignInActionResult : ActionResult {
    object Success : SignInActionResult()
    object Failed : SignInActionResult()
}
