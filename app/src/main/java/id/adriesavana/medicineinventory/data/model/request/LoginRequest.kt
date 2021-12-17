package id.adriesavana.medicineinventory.data.model.request

import androidx.annotation.Keep

@Keep
data class LoginRequest(val email: String, val password: String)