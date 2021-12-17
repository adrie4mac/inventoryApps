package id.adriesavana.medicineinventory.domain

import id.adriesavana.medicineinventory.utils.BaseUseCase
import id.adriesavana.medicineinventory.utils.DataResult
import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.utils.ActionResult
import kotlinx.coroutines.CoroutineDispatcher

class DeleteProductUseCase(
    private val repository: Repository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<DeleteProductUseCase.Params, Product, DeleteProductActionResult>(
    dispatcher
) {
    override suspend fun execute(param: Params?): DataResult<Product> {
        require(param != null) { "please added param" }
        return repository.deleteProduct(param.sku)
    }

    override suspend fun DataResult<Product>.transformToUseCaseResult(): DeleteProductActionResult =
        when (this) {
            is DataResult.Success -> DeleteProductActionResult.Success(value)
            is DataResult.Exception -> DeleteProductActionResult.Failed
        }

    data class Params(val sku: String)
}

sealed class DeleteProductActionResult : ActionResult {
    data class Success(val products: Product) : DeleteProductActionResult()
    object Failed : DeleteProductActionResult()
}
