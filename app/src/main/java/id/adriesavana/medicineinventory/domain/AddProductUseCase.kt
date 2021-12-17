package id.adriesavana.medicineinventory.domain

import id.adriesavana.medicineinventory.utils.BaseUseCase
import id.adriesavana.medicineinventory.utils.DataResult
import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.clearFormatting
import kotlinx.coroutines.CoroutineDispatcher

class AddProductUseCase(
    private val repository: Repository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<AddProductUseCase.Params, Product, AddProductActionResult>(
    dispatcher
) {
    override suspend fun execute(param: Params?): DataResult<Product> {
        require(param != null) { "please added param" }
        return repository.addProduct(
            sku = "OBT - ${param.sku}",
            productName = param.productName,
            quantity = param.quantity,
            price = param.price.clearFormatting(),
            unit = param.unit,
            status = 1,
        )
    }

    override suspend fun DataResult<Product>.transformToUseCaseResult(): AddProductActionResult =
        when (this) {
            is DataResult.Success -> AddProductActionResult.Success(value.productName)
            is DataResult.Exception -> AddProductActionResult.Failed
        }

    data class Params(val sku: String,
                      val productName: String,
                      val quantity: String,
                      val price: String,
                      val unit: String)
}

sealed class AddProductActionResult : ActionResult {
    data class Success(val productName: String) : AddProductActionResult()
    object Failed : AddProductActionResult()
}
