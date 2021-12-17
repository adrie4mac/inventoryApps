package id.adriesavana.medicineinventory.data.utils

import id.adriesavana.medicineinventory.data.model.Failure
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.HttpURLConnection

internal class NetworkResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)

        return NetworkResponseAdapter<Any>(successBodyType)
    }
}

internal class NetworkResponseAdapter<S : Any>(private val successType: Type) :
    CallAdapter<S, Call<NetworkResponse<S>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S>> {
        return NetworkResponseCall(call)
    }
}

internal class NetworkResponseCall<S : Any>(private val delegate: Call<S>) :
    Call<NetworkResponse<S>> {

    override fun enqueue(callback: Callback<NetworkResponse<S>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                with(response) {
                    val networkResponse = if (isSuccessful) {
                        body()?.let { body -> NetworkResponse.Success(body) }
                            ?: NetworkResponse.UnknownError(Throwable("Terjadi kesalahan, silahkan coba lagi.")) // Response is successful but the body is null
                    } else {
                        NetworkResponse.UnknownError(Throwable("Terjadi kesalahan, silahkan coba lagi"))
                    }

                    callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = NetworkResponse.UnknownError(throwable)
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout {
        throw Throwable(
            Failure(
                errorMessage = "Terjadi kesalahan, silahkan coba lagi",
                code = HttpURLConnection.HTTP_CLIENT_TIMEOUT
            )
        )
    }
}
