package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.LocaleRepository
import javax.inject.Inject

data class SaveLocaleUseCase @Inject constructor(private val localeRepository: LocaleRepository) {
    suspend operator fun invoke() = localeRepository.saveLocale()
}
