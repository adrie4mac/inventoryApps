package id.adriesavana.medicineinventory.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.adriesavana.medicineinventory.AppNavigationGraphDirections
import id.adriesavana.medicineinventory.domain.RegisterActionResult
import id.adriesavana.medicineinventory.domain.RegisterUseCase
import id.adriesavana.medicineinventory.ui.login.LoginFragmentDirections
import id.adriesavana.medicineinventory.utils.ActionResult
import id.adriesavana.medicineinventory.utils.BaseViewModel
import id.adriesavana.medicineinventory.utils.NavigateWithDirectionOptionsAction
import id.adriesavana.medicineinventory.utils.State
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    BaseViewModel<RegisterViewState, RegisterViewAction>(RegisterViewState()) {
    override fun renderViewState(result: ActionResult?): RegisterViewState {
        return when (result) {
            is RegisterActionResult -> result.mapRegisterActionResult()
            is RegisterViewActionResult -> result.mapRegisterViewActionResult()
            else -> getCurrentViewState()
        }
    }

    override fun handleAction(action: RegisterViewAction): LiveData<ActionResult> =
        liveData(viewModelDispatcher) {
            when (action) {
                is RegisterViewAction.OnRegister -> {
                    if (action.email.isBlank() || action.newPassword.isBlank() || action.confirmPassword.isBlank()) {
                        emit(RegisterViewActionResult.ShowErrorMessage("email or password can't empty"))
                        return@liveData
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(action.email).matches()) {
                        emit(RegisterViewActionResult.ShowErrorMessage("Invalid format email"))
                        return@liveData
                    }

                    if (action.newPassword != action.confirmPassword) {
                        emit(RegisterViewActionResult.ShowErrorMessage("Password not match"))
                        return@liveData
                    }

                    val param = RegisterUseCase.Params(action.email, action.newPassword)
                    emit(registerUseCase.getResult(param))
                }
            }
        }

    private fun RegisterActionResult.mapRegisterActionResult(): RegisterViewState {
        return when (this) {
            RegisterActionResult.Success -> getCurrentViewState().copy(
                navigateView = State(NavigateWithDirectionOptionsAction(AppNavigationGraphDirections.actionToProductList()))
            )
            RegisterActionResult.Failed -> getCurrentViewState().copy(
                messageResult = State("Incorrect email or password, please try again")
            )
        }
    }

    private fun RegisterViewActionResult.mapRegisterViewActionResult(): RegisterViewState {
        return when (this) {
            is RegisterViewActionResult.ShowErrorMessage -> getCurrentViewState().copy(
                messageResult = State(message)
            )
        }
    }
}
