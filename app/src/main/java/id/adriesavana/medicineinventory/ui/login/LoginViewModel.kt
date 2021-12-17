package id.adriesavana.medicineinventory.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.adriesavana.medicineinventory.AppNavigationGraphDirections
import id.adriesavana.medicineinventory.domain.SignInActionResult
import id.adriesavana.medicineinventory.domain.SignInUseCase
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.BaseViewModel
import id.adriesavana.medicineinventory.utils.NavigateWithDirectionOptionsAction
import id.adriesavana.medicineinventory.utils.State
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val signInUseCase: SignInUseCase) :
    BaseViewModel<LoginViewState, LoginViewAction>(LoginViewState()) {
    override fun renderViewState(result: ActionResult?): LoginViewState {
        return when (result) {
            is SignInActionResult -> result.mapSignInActionResult()
            is LoginActionResult -> result.mapLoginActionResult()
            else -> getCurrentViewState()
        }
    }

    override fun handleAction(action: LoginViewAction): LiveData<ActionResult> =
        liveData(viewModelDispatcher) {
            when (action) {
                is LoginViewAction.OnLogin -> {
                    if (action.email.isBlank() || action.password.isBlank()) {
                        emit(LoginActionResult.ShowErrorMessage("email or password can't empty"))
                        return@liveData
                    }

                    val param = SignInUseCase.Params(action.email, action.password)
                    emit(signInUseCase.getResult(param))
                }
            }
        }

    private fun SignInActionResult.mapSignInActionResult(): LoginViewState {
        return when (this) {
            SignInActionResult.Success -> getCurrentViewState().copy(
                navigateView = State(NavigateWithDirectionOptionsAction(AppNavigationGraphDirections.actionToProductList()))
            )
            SignInActionResult.Failed -> getCurrentViewState().copy(
                messageResult = State("Incorrect email or password, please try again")
            )
        }
    }

    private fun LoginActionResult.mapLoginActionResult(): LoginViewState {
        return when (this) {
            is LoginActionResult.ShowErrorMessage -> getCurrentViewState().copy(
                messageResult = State(message)
            )
        }
    }
}
