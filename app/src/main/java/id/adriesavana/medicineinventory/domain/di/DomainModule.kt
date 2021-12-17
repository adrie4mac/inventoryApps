package id.adriesavana.medicineinventory.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.adriesavana.medicineinventory.data.repository.Repository
import id.adriesavana.medicineinventory.domain.AddProductUseCase
import id.adriesavana.medicineinventory.domain.DeleteProductUseCase
import id.adriesavana.medicineinventory.domain.GetProductListUseCase
import id.adriesavana.medicineinventory.domain.RegisterUseCase
import id.adriesavana.medicineinventory.domain.SignInUseCase
import id.adriesavana.medicineinventory.domain.UpdateProductUseCase
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideProductListUseCase(
        repository: Repository,
        dispatcher: CoroutineDispatcher
    ): GetProductListUseCase {
        return GetProductListUseCase(repository, dispatcher)
    }

    @Provides
    fun provideDeleteProductUseCase(
        repository: Repository,
        dispatcher: CoroutineDispatcher
    ): DeleteProductUseCase {
        return DeleteProductUseCase(repository, dispatcher)
    }

    @Provides
    fun provideSignInUseCase(
        repository: Repository,
        dispatcher: CoroutineDispatcher
    ): SignInUseCase {
        return SignInUseCase(repository, dispatcher)
    }

    @Provides
    fun provideRegisterUseCase(
        repository: Repository,
        dispatcher: CoroutineDispatcher
    ): RegisterUseCase {
        return RegisterUseCase(repository, dispatcher)
    }

    @Provides
    fun provideAddProductUseCase(
        repository: Repository,
        dispatcher: CoroutineDispatcher
    ): AddProductUseCase {
        return AddProductUseCase(repository, dispatcher)
    }

    @Provides
    fun provideUpdateProductUseCase(
        repository: Repository,
        dispatcher: CoroutineDispatcher
    ): UpdateProductUseCase {
        return UpdateProductUseCase(repository, dispatcher)
    }
}
