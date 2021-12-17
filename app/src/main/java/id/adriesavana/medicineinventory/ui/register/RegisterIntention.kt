package id.adriesavana.medicineinventory.ui.register

import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.NavControllerAction
import id.adriesavana.medicineinventory.utils.State
import id.adriesavana.medicineinventory.utils.ViewAction
import id.adriesavana.medicineinventory.utils.ViewState

data class RegisterViewState(
    val navigateView: State<NavControllerAction?> = State(null),
    val messageResult: State<String> = State(""),
) : ViewState

sealed class RegisterViewAction : ViewAction {
    data class OnRegister(
        val email: String,
        val newPassword: String,
        val confirmPassword: String
    ) : RegisterViewAction()
}

sealed class RegisterViewActionResult : ActionResult {
    data class ShowErrorMessage(val message: String) : RegisterViewActionResult()
}
