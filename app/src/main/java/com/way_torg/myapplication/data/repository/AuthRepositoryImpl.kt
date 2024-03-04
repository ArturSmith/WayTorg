package com.way_torg.myapplication.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.way_torg.myapplication.domain.repository.AuthRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebase_auth: FirebaseAuth
) : AuthRepository {
    override suspend fun signIn( password: String): Result<Boolean> {
        return try {
            val user = firebase_auth.signInWithEmailAndPassword(EMAIL, password).await().user
            Result.success(user != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            firebase_auth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override fun getAuthState() = callbackFlow {
        firebase_auth.addAuthStateListener {
            trySend(it.currentUser != null)
        }
        awaitClose {
            this.cancel()
        }
    }

    private companion object {
        const val EMAIL = "khajievkhalid@gmail.com"
    }
}