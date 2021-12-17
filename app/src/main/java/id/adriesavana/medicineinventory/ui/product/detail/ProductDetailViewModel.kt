package id.adriesavana.medicineinventory.ui.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.adriesavana.medicineinventory.domain.DeleteProductActionResult
import id.adriesavana.medicineinventory.domain.DeleteProductUseCase
import id.adriesavana.medicineinventory.domain.UpdateProductActionResult
import id.adriesavana.medicineinventory.domain.UpdateProductUseCase
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.BaseViewModel
import id.adriesavana.medicineinventory.utils.State
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val deleteProductUseCase: DeleteProductUseCase,
                                                 private val updateProductUseCase: UpdateProductUseCase) :
    BaseViewModel<ProductDetailViewState, ProductDetailViewAction>(ProductDetailViewState()) {

    override fun renderViewState(result: ActionResult?): ProductDetailViewState {
        return when (result) {
            is ProductDetailActionResult.ShowProduct -> result.mapProductDetailActionResult()
            is DeleteProductActionResult -> result.mapDeleteProductActionResult()
            is UpdateProductActionResult -> result.mapUpdateProductActionResult()
            else -> getCurrentViewState()
        }
    }

    override fun handleAction(action: ProductDetailViewAction): LiveData<ActionResult> =
        liveData(viewModelDispatcher) {
            when (action) {
                is ProductDetailViewAction.InitProduct -> {
                    emit(ProductDetailActionResult.ShowProduct(action.product))
                }
                is ProductDetailViewAction.DeleteProduct -> {
                    val param = DeleteProductUseCase.Params(action.product.sku)
                    emit(deleteProductUseCase.getResult(param))
                }
                is ProductDetailViewAction.UpdateProduct -> {
                    if (action.sku.isBlank()) {
                        emit(ProductDetailActionResult.ShowErrorMessage("Sku can't be empty"))
                        return@liveData
                    }

                    if (action.productName.isBlank()) {
                        emit(ProductDetailActionResult.ShowErrorMessage("Product name can't be empty"))
                        return@liveData
                    }

                    if (action.price.isBlank()) {
                        emit(ProductDetailActionResult.ShowErrorMessage("Price can't be empty"))
                        return@liveData
                    }

                    if (action.quantity.isBlank()) {
                        emit(ProductDetailActionResult.ShowErrorMessage("Quantity can't be empty"))
                        return@liveData
                    }

                    if (action.unit.isBlank()) {
                        emit(ProductDetailActionResult.ShowErrorMessage("Unit can't be empty"))
                        return@liveData
                    }

                    val params = UpdateProductUseCase.Params(
                        sku = action.sku,
                        productName = action.productName,
                        price = action.price,
                        unit = action.unit,
                        quantity = action.quantity,
                    )
                    emit(updateProductUseCase.getResult(params))
                }
            }
        }

    private fun ProductDetailActionResult.mapProductDetailActionResult(): ProductDetailViewState {
        return when (this) {
            is ProductDetailActionResult.ShowProduct -> getCurrentViewState().copy(
                sku = State(product.sku),
                price = State(product.price.toString()),
                quantity = State(product.quantity.toString()),
                unit = State(product.unit),
                productName = State(product.productName),
            )
            is ProductDetailActionResult.ShowErrorMessage -> getCurrentViewState().copy(
                messageResult = State(message)
            )
        }
    }

    private fun UpdateProductActionResult.mapUpdateProductActionResult(): ProductDetailViewState {
        return when (this) {
            is UpdateProductActionResult.Success -> getCurrentViewState().copy(
                messageResult = State("Success update $productName")
            )
            UpdateProductActionResult.Failed -> getCurrentViewState().copy(
                messageResult = State("Failed update product")
            )
        }
    }

    private fun DeleteProductActionResult.mapDeleteProductActionResult(): ProductDetailViewState {
        return when (this) {
            is DeleteProductActionResult.Success -> getCurrentViewState().copy(
                messageResult = State("Product ${products.productName} has been delete"),
                navigateUp = State(Unit)
            )
            DeleteProductActionResult.Failed -> getCurrentViewState().copy(
                messageResult = State("Failed delete product")
            )
        }
    }
}
