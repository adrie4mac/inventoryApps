package id.adriesavana.medicineinventory.utils

sealed class DataResult<out T> {
    data class Success<T>(val value: T) : DataResult<T>()
    object Exception : DataResult<Nothing>()
}
