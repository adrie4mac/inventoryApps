package id.adriesavana.medicineinventory.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.adriesavana.medicineinventory.data.datasource.NetworkDataSource
import id.adriesavana.medicineinventory.data.datasource.NetworkDataSourceImpl
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.data.repository.RepositoryImpl
import id.adriesavana.medicineinventory.data.service.ApiService
import id.adriesavana.medicineinventory.data.utils.NetworkResponseAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false)
            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hoodwink.medkomtek.net/api/")
            .client(okHttpClient)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(retrofit: Retrofit): NetworkDataSource {
        return NetworkDataSourceImpl(apiService = retrofit.create(ApiService::class.java))
    }

    @Provides
    @Singleton
    fun provideRepository(dataSource: NetworkDataSource): Repository {
        return RepositoryImpl(dataSource)
    }
}
