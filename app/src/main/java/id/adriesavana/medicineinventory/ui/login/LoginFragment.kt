package id.adriesavana.medicineinventory.ui.login

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
class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()

    private val tvLoginRegistration by bind<AppCompatTextView>(R.id.tv_login_registration)
    private val etInputEmail by bind<AppCompatEditText>(R.id.et_input_email)
    private val etInputPassword by bind<AppCompatEditText>(R.id.et_input_password)
    private val btnSignIn by bind<MaterialButton>(R.id.btn_sign_in)

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
            append("Not a member? ")
            bold {
                inSpans(
                    getclickableSpan(
                        spanColor = colorPrimary,
                        action = {
                            it.findNavController().navigate(R.id.action_to_registration)
                        })
                ) { append("Register now") }
            }
        }
        tvLoginRegistration.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = landingRegisterDescription
        }

        btnSignIn.onClick {
            requireActivity().hideKeyboard()
            viewModel.onAction(
                LoginViewAction.OnLogin(
                    email = etInputEmail.text.toString(),
                    password = etInputPassword.text.toString()
                )
            )
        }
    }
}
