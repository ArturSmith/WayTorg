package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() =
        authRepository.signOut()
}