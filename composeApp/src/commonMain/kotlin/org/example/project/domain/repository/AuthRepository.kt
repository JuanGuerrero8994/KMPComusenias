package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.data.core.Resource
import org.example.project.domain.model.user.User

interface AuthRepository {
    suspend fun signIn(user: User): Flow<Resource<User>>
    suspend fun signUp(user: User): Flow<Resource<User>>
    suspend fun signOut(): Flow<Resource<String>>
    suspend fun getCurrentUser():Flow<Resource<User?>>
    suspend fun resetPassword(email: String): Flow<Resource<String>>

}