package id.adriesavana.medicineinventory.data.model

import androidx.annotation.Keep
import java.io.IOException

@Keep
open class Failure(
    val code: Int,
    val errorMessage: String
) : IOException(errorMessage)
