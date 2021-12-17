package id.adriesavana.medicineinventory.ui.product.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.adriesavana.medicineinventory.domain.AddProductActionResult
import id.adriesavana.medicineinventory.domain.AddProductUseCase
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.BaseViewModel
import id.adriesavana.medicineinventory.utils.State
import javax.inject.Inject

@HiltViewModel
class AddNewProductViewModel @Inject constructor(private val addProductUseCase: AddProductUseCase) :
    BaseViewModel<AddNewProductViewState, AddNewProductViewAction>(AddNewProductViewState()) {

    override fun renderViewState(result: ActionResult?): AddNewProductViewState {
        return when (result) {
            is AddProductActionResult -> result.mapAddProductActionResult()
            is AddNewProductViewActionResult -> result.mapAddNewProductViewActionResult()
            else -> getCurrentViewState()
        }
    }

    override fun handleAction(action: AddNewProductViewAction): LiveData<ActionResult> =
        liveData(viewModelDispatcher) {
            when (action) {
                is AddNewProductViewAction.AddProduct -> {
                    if (action.sku.isBlank()) {
                        emit(AddNewProductViewActionResult.ShowErrorMessage("Sku can't be empty"))
                        return@liveData
                    }

                    if (action.productName.isBlank()) {
                        emit(AddNewProductViewActionResult.ShowErrorMessage("Product name can't be empty"))
                        return@liveData
                    }

                    if (action.price.isBlank()) {
                        emit(AddNewProductViewActionResult.ShowErrorMessage("Price can't be empty"))
                        return@liveData
                    }

                    if (action.quantity.isBlank()) {
                        emit(AddNewProductViewActionResult.ShowErrorMessage("Quantity can't be empty"))
                        return@liveData
                    }

                    if (action.unit.isBlank()) {
                        emit(AddNewProductViewActionResult.ShowErrorMessage("Unit can't be empty"))
                        return@liveData
                    }

                    val params = AddProductUseCase.Params(
                        sku = action.sku,
                        productName = action.productName,
                        price = action.price,
                        unit = action.unit,
                        quantity = action.quantity,
                    )
                    emit(addProductUseCase.getResult(params))
                }
            }
        }

    private fun AddNewProductViewActionResult.mapAddNewProductViewActionResult(): AddNewProductViewState {
        return when (this) {
            is AddNewProductViewActionResult.ShowErrorMessage -> getCurrentViewState().copy(
                messageResult = State(message)
            )
        }
    }

    private fun AddProductActionResult.mapAddProductActionResult(): AddNewProductViewState {
        return when (this) {
            is AddProductActionResult.Success -> getCurrentViewState().copy(
                messageResult = State("Success Added $productName"),
                navigateUp = State(Unit)
            )
            AddProductActionResult.Failed -> getCurrentViewState().copy(
                messageResult = State("Failed added product")
            )
        }
    }
}
