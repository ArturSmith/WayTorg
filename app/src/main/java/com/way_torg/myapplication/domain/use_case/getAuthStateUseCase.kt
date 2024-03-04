package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class getAuthStateUseCase @Inject constructor(private val authRepository: AuthRepository) {
     operator fun invoke() = authRepository.getAuthState()
}