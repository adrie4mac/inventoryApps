package id.adriesavana.medicineinventory.data.datasource

import id.adriesavana.medicineinventory.data.model.request.DeleteRequest
import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.data.model.request.AddProductRequest
import id.adriesavana.medicineinventory.data.model.request.LoginRequest
import id.adriesavana.medicineinventory.data.model.request.RegisterRequest
import id.adriesavana.medicineinventory.data.model.request.UpdateProductRequest
import id.adriesavana.medicineinventory.data.service.ApiService
import id.adriesavana.medicineinventory.utils.DataResult
import id.adriesavana.medicineinventory.data.utils.NetworkResponse

internal class NetworkDataSourceImpl(private val apiService: ApiService) : NetworkDataSource {
    private var authorization: String = "Bearer "

    override suspend fun register(email: String, password: String): DataResult<String> {
        return when (val result = apiService.register(RegisterRequest(email, password))) {
            is NetworkResponse.Success -> DataResult.Success(result.toString())
            else -> DataResult.Exception
        }
    }

    override suspend fun signIn(email: String, password: String): DataResult<String> {
        return when (val result = apiService.signIn(LoginRequest(email, password))) {
            is NetworkResponse.Success -> {
                authorization = "Bearer ${result.body.token}"
                DataResult.Success(result.body.token)
            }
            else -> DataResult.Exception
        }
    }

    override suspend fun getProductList(): DataResult<List<Product>> {
        return when (val result = apiService.getProductList()) {
            is NetworkResponse.Success -> DataResult.Success(result.body)
            else -> DataResult.Exception
        }
    }

    override suspend fun deleteProduct(sku: String): DataResult<Product> {
        return when (val result = apiService.deleteProduct(authorization, DeleteRequest(sku))) {
            is NetworkResponse.Success -> DataResult.Success(result.body)
            else -> DataResult.Exception
        }
    }

    override suspend fun addProduct(
        sku: String,
        productName: String,
        quantity: String,
        price: String,
        unit: String,
        status: Int
    ): DataResult<Product> {
        val payload = AddProductRequest(
            sku = sku,
            productName = productName,
            quantity = quantity.toInt(),
            price = price.toInt(),
            unit = unit,
            status = status,
        )
        return when (val result = apiService.addProduct(authorization, payload)) {
            is NetworkResponse.Success -> DataResult.Success(result.body)
            else -> DataResult.Exception
        }
    }

    override suspend fun updateProduct(
        sku: String,
        productName: String,
        quantity: String,
        price: String,
        unit: String,
        status: Int
    ): DataResult<Product> {
        val payload = UpdateProductRequest(
            sku = sku,
            productName = productName,
            quantity = quantity.toInt(),
            price = price.toInt(),
            unit = unit,
            status = status,
        )
        return when (val result = apiService.updateProduct(authorization, payload)) {
            is NetworkResponse.Success -> DataResult.Success(result.body)
            else -> DataResult.Exception
        }
    }
}
