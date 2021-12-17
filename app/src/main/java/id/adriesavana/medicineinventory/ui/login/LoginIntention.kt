package id.adriesavana.medicineinventory.ui.login

import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.NavControllerAction
import id.adriesavana.medicineinventory.utils.State
import id.adriesavana.medicineinventory.utils.ViewAction
import id.adriesavana.medicineinventory.utils.ViewState

data class LoginViewState(
    val navigateView: State<NavControllerAction?> = State(null),
    val messageResult: State<String> = State(""),
) : ViewState

sealed class LoginViewAction : ViewAction {
    data class OnLogin(val email: String, val password: String) : LoginViewAction()
}

sealed class LoginActionResult : ActionResult {
    data class ShowErrorMessage(val message: String) : LoginActionResult()
}
