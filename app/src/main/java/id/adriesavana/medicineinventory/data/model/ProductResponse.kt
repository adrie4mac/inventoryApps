package id.adriesavana.medicineinventory.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import id.adriesavana.medicineinventory.utils.LocaleId
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ProductResponse(
    override val id: Int,
    override val sku: String,
    @SerializedName(value = "product_name") override val productName: String,
    @SerializedName(value = "qty") override val quantity: Int,
    override val price: Int,
    override val unit: String,
    override val image: String,
    override val status: Int,
    @SerializedName(value = "created_at") override val createDate: String,
    @SerializedName(value = "updated_at") override val updateDate: String,
    @SerializedName(value = "success") override val isSuccess: Boolean = true
) : Product
