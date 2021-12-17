package id.adriesavana.medicineinventory.domain

import id.adriesavana.medicineinventory.utils.BaseUseCase
import id.adriesavana.medicineinventory.utils.DataResult
import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.clearFormatting
import kotlinx.coroutines.CoroutineDispatcher

class UpdateProductUseCase(
    private val repository: Repository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<UpdateProductUseCase.Params, Product, UpdateProductActionResult>(
    dispatcher
) {
    override suspend fun execute(param: Params?): DataResult<Product> {
        require(param != null) { "please added param" }
        val skuParam = if (param.sku.contains("OBT -")) param.sku else "OBT - ${param.sku}"
        return repository.updateProduct(
            sku = skuParam,
            productName = param.productName,
            quantity = param.quantity,
            price = param.price.clearFormatting(),
            unit = param.unit,
            status = 1,
        )
    }

    override suspend fun DataResult<Product>.transformToUseCaseResult(): UpdateProductActionResult =
        when (this) {
            is DataResult.Success -> {
                if (value.isSuccess) {
                    UpdateProductActionResult.Success(value.productName)
                }

                UpdateProductActionResult.Failed

            }
            is DataResult.Exception -> UpdateProductActionResult.Failed
        }

    data class Params(val sku: String,
                      val productName: String,
                      val quantity: String,
                      val price: String,
                      val unit: String)
}

sealed class UpdateProductActionResult : ActionResult {
    data class Success(val productName: String) : UpdateProductActionResult()
    object Failed : UpdateProductActionResult()
}
