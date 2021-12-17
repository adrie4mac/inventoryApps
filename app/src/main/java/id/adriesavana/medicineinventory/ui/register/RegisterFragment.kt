package id.adriesavana.medicineinventory.ui.register

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import id.adriesavana.medicineinventory.R
import id.adriesavana.medicineinventory.utils.BaseFragment
import id.adriesavana.medicineinventory.utils.bind
import id.adriesavana.medicineinventory.utils.getclickableSpan
import id.adriesavana.medicineinventory.utils.hideKeyboard
import id.adriesavana.medicineinventory.utils.onClick
import id.adriesavana.medicineinventory.utils.onValueChanged

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    private val viewModel: RegisterViewModel by viewModels()

    private val tvSignIn by bind<AppCompatTextView>(R.id.tv_sign_in)
    private val etInputEmail by bind<AppCompatEditText>(R.id.et_input_email)
    private val etInputNewPassword by bind<AppCompatEditText>(R.id.et_input_new_password)
    private val etInputConfirmPassword by bind<AppCompatEditText>(R.id.et_input_confirm_new_password)
    private val btnRegister by bind<MaterialButton>(R.id.btn_register)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            viewState.navigateView.onValueChanged(::navigateView)
            viewState.messageResult.onValueChanged(::showErrorMessage)
        }
    }

    private fun initView() {
        val colorPrimary = ContextCompat.getColor(requireContext(), R.color.han_blue)
        val landingRegisterDescription = buildSpannedString {
            append("Already a member? ")
            bold {
                inSpans(
                    getclickableSpan(
                        spanColor = colorPrimary,
                        action = {
                            it.findNavController().navigateUp()
                        })
                ) { append("Sign In") }
            }
        }
        tvSignIn.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = landingRegisterDescription
        }

        btnRegister.onClick {
            requireActivity().hideKeyboard()
            viewModel.onAction(
                RegisterViewAction.OnRegister(
                    email = etInputEmail.text.toString(),
                    newPassword = etInputNewPassword.text.toString(),
                    confirmPassword = etInputConfirmPassword.text.toString(),
                )
            )
        }
    }
}
