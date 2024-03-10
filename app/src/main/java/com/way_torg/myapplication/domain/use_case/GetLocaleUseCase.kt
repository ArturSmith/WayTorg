package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.LocaleRepository
import javax.inject.Inject

data class GetLocaleUseCase @Inject constructor(private val localeRepository: LocaleRepository) {
    operator fun invoke() = localeRepository.getLocale()
}
