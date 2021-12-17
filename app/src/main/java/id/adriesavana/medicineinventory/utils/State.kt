package id.adriesavana.medicineinventory.utils

class State<out T>(private val value: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the value and prevents its use again.
     */
    fun getValueIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            value
        }
    }

    /**
     * Returns the value, even if it's already been handled.
     */
    fun peekValue(): T = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false

        other as State<*>

        if (value != other.value) return false
        if (hasBeenHandled != other.hasBeenHandled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + hasBeenHandled.hashCode()
        return result
    }

    override fun toString(): String {
        return "State(" +
                "hasBeenHandled=$hasBeenHandled, " +
                "value=$value" +
                ")"
    }


}
