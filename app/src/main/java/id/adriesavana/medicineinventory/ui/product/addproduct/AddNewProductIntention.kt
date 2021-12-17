package id.adriesavana.medicineinventory.ui.product.addproduct

import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.State
import id.adriesavana.medicineinventory.utils.ViewAction
import id.adriesavana.medicineinventory.utils.ViewState

data class AddNewProductViewState(
    val navigateUp: State<Unit?> = State(null),
    val messageResult: State<String> = State(""),
) : ViewState

sealed class AddNewProductViewAction : ViewAction {
    data class AddProduct(
        val sku: String,
        val productName: String,
        val quantity: String,
        val price: String,
        val unit: String
    ) : AddNewProductViewAction()
}

sealed class AddNewProductViewActionResult : ActionResult {
    data class ShowErrorMessage(val message: String) : AddNewProductViewActionResult()
}