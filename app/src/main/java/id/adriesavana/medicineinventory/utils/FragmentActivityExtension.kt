package id.adriesavana.medicineinventory.utils

import android.app.Activity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val windowToken = findViewById<ViewGroup>(android.R.id.content).rootView.windowToken
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.showKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}
