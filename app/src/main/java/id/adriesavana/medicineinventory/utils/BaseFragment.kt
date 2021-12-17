package id.adriesavana.medicineinventory.utils

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // since some screen doesn't need to resize the screen
        // (ex: Button on the bottom isn't always go to top while keyboard is shown)
        // so we need to used ADJUST_PAN instead of ADJUST_RESIZE
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor()
    }

    protected fun excludeTransitions() {
        reenterTransition = null
        exitTransition = null
        enterTransition = null
        returnTransition = null
    }

    private fun setStatusBarColor(
        @ColorRes colorId: Int = android.R.color.white,
        lightStatusBarIcon: Boolean = false
    ) {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), colorId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        // update status bar icon's color (dark or light)
        with(activity?.window?.decorView) {
            this?.setSystemUiVisibility(
                if (lightStatusBarIcon) {
                    this.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                } else {
                    this.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            )
        }
    }

    protected inline fun handlePhysicalBackPressed(crossinline listener: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    listener.invoke()
                }
            })
    }

    protected fun navigateUp() {
        findNavController().navigateUp()
    }

    protected fun navigateView(nav: NavControllerAction?) {
        if (nav == null) return
        nav(findNavController())
    }

    protected fun showErrorMessage(message: String) {
        if (message.isBlank()) return
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun onBackPrevPage(unit: Unit?) {
        if (unit == null) return
        navigateUp()
    }
}
