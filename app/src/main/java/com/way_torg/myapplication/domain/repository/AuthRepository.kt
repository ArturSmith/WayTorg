package com.way_torg.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(password: String): Result<Boolean>
    suspend fun signOut(): Result<Boolean>

    fun getAuthState(): Flow<Boolean>

}