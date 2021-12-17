package id.adriesavana.medicineinventory.data.repository

import id.adriesavana.medicineinventory.data.datasource.NetworkDataSource

internal class RepositoryImpl(private val networkDataSource: NetworkDataSource) : Repository,
    NetworkDataSource by networkDataSource
