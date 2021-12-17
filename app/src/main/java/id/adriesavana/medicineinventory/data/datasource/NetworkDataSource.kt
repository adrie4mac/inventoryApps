package id.adriesavana.medicineinventory.data.datasource

import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.utils.DataResult

interface NetworkDataSource {
    suspend fun getProductList(): DataResult<List<Product>>
    suspend fun deleteProduct(sku: String): DataResult<Product>
    suspend fun signIn(email: String, password: String): DataResult<String>
    suspend fun register(email: String, password: String): DataResult<String>
    suspend fun addProduct(
        sku: String,
        productName: String,
        quantity: String,
        price: String,
        unit: String,
        status: Int
    ): DataResult<Product>

    suspend fun updateProduct(
        sku: String,
        productName: String,
        quantity: String,
        price: String,
        unit: String,
        status: Int
    ): DataResult<Product>
}
