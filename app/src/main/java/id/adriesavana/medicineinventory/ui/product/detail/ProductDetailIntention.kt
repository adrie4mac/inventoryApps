package id.adriesavana.medicineinventory.ui.product.detail

import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.ui.register.RegisterViewActionResult
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.NavControllerAction
import id.adriesavana.medicineinventory.utils.State
import id.adriesavana.medicineinventory.utils.ViewAction
import id.adriesavana.medicineinventory.utils.ViewState

data class ProductDetailViewState(
    val navigateView: State<NavControllerAction?> = State(null),
    val navigateUp: State<Unit?> = State(null),
    val sku: State<String> = State(""),
    val productName: State<String> = State(""),
    val quantity: State<String> = State(""),
    val price: State<String> = State(""),
    val unit: State<String> = State(""),
    val image: State<String> = State(""),
    val status: State<String> = State(""),
    val messageResult: State<String> = State(""),
) : ViewState

sealed class ProductDetailViewAction : ViewAction {
    data class InitProduct(val product: Product) : ProductDetailViewAction()
    data class  DeleteProduct(val product: Product) : ProductDetailViewAction()
    data class UpdateProduct(
        val sku: String,
        val productName: String,
        val quantity: String,
        val price: String,
        val unit: String
    ) : ProductDetailViewAction()
}

sealed class ProductDetailActionResult : ActionResult {
    data class ShowProduct(val product: Product) : ProductDetailActionResult()
    data class ShowErrorMessage(val message: String) : ProductDetailActionResult()
}
