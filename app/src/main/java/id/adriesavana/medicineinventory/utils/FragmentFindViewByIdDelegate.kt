package id.adriesavana.medicineinventory.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentFindViewByIdDelegate<T : View>(
    fragment: Fragment,
    @IdRes private val resourceId: Int,
) : ReadOnlyProperty<Fragment, T> {

    private var view: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(owner, ::destroyView)
            }
        })
    }

    private fun destroyView(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                val destroyableView = view
                if (destroyableView is DestroyableView) destroyableView.onViewDestroy()
                view = null
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val currentView = view
        if (currentView != null) return currentView
        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("View should not be called before onCreateView and after onDestroyView.")
        }
        return thisRef.requireView().findViewById<T>(resourceId).also { view = it }
    }
}

interface DestroyableView {
    fun onViewDestroy()
}
