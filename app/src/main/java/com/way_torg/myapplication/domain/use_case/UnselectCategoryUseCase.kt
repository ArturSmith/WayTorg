package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.repository.CategoryRepository
import javax.inject.Inject

data class UnselectCategoryUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(id:String) = repository.unselectCategory(id)
}
