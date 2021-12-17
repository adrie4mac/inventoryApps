package id.adriesavana.medicineinventory.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<VMViewState : ViewState, VMViewAction : ViewAction>(initialState: VMViewState) :
    ViewModel() {

    private val viewAction = MutableLiveData<VMViewAction>()
    val viewState: LiveData<VMViewState> =
        Transformations.map(Transformations.switchMap(viewAction, ::handleAction)) {
            renderViewState(it).updateCurrentViewState()
        }

    protected val viewModelDispatcher: CoroutineContext
        get() = viewModelScope.coroutineContext

    private var currentViewState: VMViewState = initialState

    protected abstract fun renderViewState(result: ActionResult?): VMViewState
    protected abstract fun handleAction(action: VMViewAction): LiveData<ActionResult>
    protected fun getCurrentViewState(): VMViewState = currentViewState

    private fun VMViewState.updateCurrentViewState(): VMViewState = this.also {
        currentViewState = it
    }

    @MainThread
    fun onAction(action: VMViewAction) {
        viewAction.value = action
    }
}
