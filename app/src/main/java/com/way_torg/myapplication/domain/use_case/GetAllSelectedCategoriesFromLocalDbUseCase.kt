package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.CategoryRepository
import javax.inject.Inject

data class GetAllSelectedCategoriesFromLocalDbUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke() = repository.getSelectedCategoriesFromLocalDb()
}
