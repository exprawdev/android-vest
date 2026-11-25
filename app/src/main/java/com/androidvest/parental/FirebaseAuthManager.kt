package com.androidvest.parental

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun registerParent(
        email: String,
        password: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(
            email.trim(),
            password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                if (user != null) {
                    onResult(Result.success(user))
                } else {
                    onResult(
                        Result.failure(
                            Exception("Account created but user data is unavailable.")
                        )
                    )
                }
            } else {
                onResult(
                    Result.failure(
                        task.exception ?: Exception("Parent registration failed.")
                    )
                )
            }
        }
    }

    fun signInParent(
        email: String,
        password: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(
            email.trim(),
            password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                if (user != null) {
                    onResult(Result.success(user))
                } else {
                    onResult(
                        Result.failure(
                            Exception("Login successful but user data is unavailable.")
                        )
                    )
                }
            } else {
                onResult(
                    Result.failure(
                        task.exception ?: Exception("Parent login failed.")
                    )
                )
            }
        }
    }

    fun signInChildAnonymously(
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser != null && currentUser.isAnonymous) {
            onResult(Result.success(currentUser))
            return
        }

        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {
                        onResult(Result.success(user))
                    } else {
                        onResult(
                            Result.failure(
                                Exception("Child identity is unavailable.")
                            )
                        )
                    }
                } else {
                    onResult(
                        Result.failure(
                            task.exception ?: Exception("Child authentication failed.")
                        )
                    )
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}
