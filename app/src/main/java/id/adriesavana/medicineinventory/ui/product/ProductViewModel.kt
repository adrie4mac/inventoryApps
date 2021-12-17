package id.adriesavana.medicineinventory.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.adriesavana.medicineinventory.domain.GetProductListActionResult
import id.adriesavana.medicineinventory.domain.GetProductListUseCase
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.BaseViewModel
import id.adriesavana.medicineinventory.utils.State
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val getProductListUseCase: GetProductListUseCase) :
    BaseViewModel<ProductViewState, ProductViewAction>(ProductViewState()) {
    override fun renderViewState(result: ActionResult?): ProductViewState {
        return when (result) {
            is GetProductListActionResult -> result.mapGetProductListActionResult()
            else -> getCurrentViewState()
        }
    }

    override fun handleAction(action: ProductViewAction): LiveData<ActionResult> =
        liveData(viewModelDispatcher) {
            when (action) {
                ProductViewAction.LoadProduct -> {
                    emit(getProductListUseCase.getResult())
                }
            }
        }

    private fun GetProductListActionResult.mapGetProductListActionResult(): ProductViewState {
        return when (this) {
            is GetProductListActionResult.Success -> getCurrentViewState().copy(
                hideLoadMore = State(Unit),
                loadProducts = State(products),
            )
            GetProductListActionResult.Failed -> getCurrentViewState().copy(
                hideLoadMore = State(Unit),
            )
        }
    }
}
