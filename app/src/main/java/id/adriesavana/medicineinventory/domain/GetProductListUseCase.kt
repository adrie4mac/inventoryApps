package id.adriesavana.medicineinventory.domain

import id.adriesavana.medicineinventory.utils.BaseUseCase
import id.adriesavana.medicineinventory.utils.DataResult
import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.utils.ActionResult
import kotlinx.coroutines.CoroutineDispatcher

class GetProductListUseCase(
    private val repository: Repository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<Nothing, List<Product>, GetProductListActionResult>(
    dispatcher
) {
    override suspend fun execute(param: Nothing?): DataResult<List<Product>> {
        return repository.getProductList()
    }

    override suspend fun DataResult<List<Product>>.transformToUseCaseResult(): GetProductListActionResult =
        when (this) {
            is DataResult.Success -> GetProductListActionResult.Success(value)
            is DataResult.Exception -> GetProductListActionResult.Failed
        }
}

sealed class GetProductListActionResult : ActionResult {
    data class Success(val products: List<Product>) : GetProductListActionResult()
    object Failed : GetProductListActionResult()
}
