package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.FilterRepository
import com.way_torg.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

data class GetAllFiltersUseCase @Inject constructor(private val repository: FilterRepository) {
    suspend operator fun invoke() = repository.getAllFilters()
}
