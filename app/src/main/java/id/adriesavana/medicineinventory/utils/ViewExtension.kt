package id.adriesavana.medicineinventory.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

fun <T : View> Fragment.bind(@IdRes resId: Int) =
    FragmentFindViewByIdDelegate<T>(this, resId)

inline fun <T> State<T>?.onValueChanged(listener: (T) -> Unit) {
    this?.getValueIfNotHandled()?.let(listener)
}

fun getclickableSpan(
    spanColor: Int,
    showUnderlineText: Boolean = false,
    action: (view: View) -> Unit
): ClickableSpan {
    return object : ClickableSpan() {
        override fun onClick(view: View) {
            action(view)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.apply {
                isUnderlineText = showUnderlineText
                color = spanColor
            }
        }
    }
}

typealias NavControllerAction = (NavController) -> Unit

private inline fun NavController.tryNavigate(
    onFailedNavigate: NavControllerAction = {},
    action: NavController.() -> Boolean,
) {
    var success = false
    try {
        success = action()
    } catch (ex: Throwable) {
        Log.e("Nav", ex.localizedMessage)
    } finally {
        if (!success) onFailedNavigate(this)
    }
}

fun NavController.safeNavigate(
    navDirections: NavDirections,
    navOptions: NavOptions? = null,
    onFailedNavigate: NavControllerAction = {},
) {
    tryNavigate(onFailedNavigate) {
        val action = currentDestination?.getAction(navDirections.actionId) ?: graph.getAction(
            navDirections.actionId
        )
        if (action != null && currentDestination?.id != action.destinationId) {
            navigate(navDirections, navOptions)
            return@tryNavigate true
        }
        return@tryNavigate false
    }
}

data class NavigateWithDirectionOptionsAction(
    val navDirections: NavDirections,
    private val navOptions: NavOptions? = null,
    private val onFailedNavigate: NavControllerAction = {},
) : NavControllerAction {
    override fun invoke(navController: NavController) {
        navController.safeNavigate(navDirections, navOptions, onFailedNavigate)
    }
}

inline fun <V : View> V.onClick(crossinline listener: (V) -> Unit) {
    this.setOnClickListener {
        listener.invoke(this)
    }
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableRes)

fun String.clearFormatting(): String = this.replace(".", "", true)