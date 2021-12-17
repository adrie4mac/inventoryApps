package id.adriesavana.medicineinventory.data.model

import android.os.Parcelable

interface Product: Parcelable {
    val id: Int
    val sku: String
    val productName: String
    val quantity: Int
    val price: Int
    val unit: String
    val image: String
    val status: Int
    val createDate: String
    val updateDate: String
    val isSuccess: Boolean
}