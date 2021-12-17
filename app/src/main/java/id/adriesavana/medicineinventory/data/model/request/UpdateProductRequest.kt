package id.adriesavana.medicineinventory.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateProductRequest(
    val sku: String,
    @SerializedName(value = "product_name") val productName: String,
    @SerializedName(value = "qty") val quantity: Int,
    val price: Int,
    val unit: String,
    val status: Int
)
