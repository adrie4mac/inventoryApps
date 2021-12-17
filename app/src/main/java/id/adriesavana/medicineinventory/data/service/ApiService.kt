package id.adriesavana.medicineinventory.data.service

import com.google.gson.JsonObject
import id.adriesavana.medicineinventory.data.model.request.DeleteRequest
import id.adriesavana.medicineinventory.data.model.ProductResponse
import id.adriesavana.medicineinventory.data.model.SignInResponse
import id.adriesavana.medicineinventory.data.model.request.AddProductRequest
import id.adriesavana.medicineinventory.data.model.request.LoginRequest
import id.adriesavana.medicineinventory.data.model.request.RegisterRequest
import id.adriesavana.medicineinventory.data.model.request.UpdateProductRequest
import id.adriesavana.medicineinventory.data.utils.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

internal interface ApiService {

    @POST("register")
    suspend fun register(@Body body: RegisterRequest): NetworkResponse<JsonObject>

    @POST("auth/login")
    suspend fun signIn(@Body body: LoginRequest): NetworkResponse<SignInResponse>

    @GET("items")
    suspend fun getProductList(): NetworkResponse<List<ProductResponse>>

    @POST("item/add")
    suspend fun addProduct(@Header("Authorization") authHeader: String, @Body body: AddProductRequest): NetworkResponse<ProductResponse>

    @POST("item/update")
    suspend fun updateProduct(@Header("Authorization") authHeader: String, @Body body: UpdateProductRequest): NetworkResponse<ProductResponse>

    @POST("item/delete")
    suspend fun deleteProduct(@Header("Authorization") authHeader: String, @Body body: DeleteRequest): NetworkResponse<ProductResponse>
}
