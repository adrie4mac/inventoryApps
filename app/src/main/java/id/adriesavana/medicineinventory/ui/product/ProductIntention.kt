package id.adriesavana.medicineinventory.ui.product

import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.utils.NavControllerAction
import id.adriesavana.medicineinventory.utils.State
import id.adriesavana.medicineinventory.utils.ViewAction
import id.adriesavana.medicineinventory.utils.ViewState

data class ProductViewState(
    val navigateView: State<NavControllerAction?> = State(null),
    val loadProducts: State<List<Product>?> = State(null),
    val hideLoadMore: State<Unit?> = State(null),
) : ViewState

sealed class ProductViewAction : ViewAction {
    object LoadProduct : ProductViewAction()
}