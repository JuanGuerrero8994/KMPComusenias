package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.data.core.Resource
import org.example.project.data.model.auth.RequestUser
import org.example.project.domain.model.auth.User

interface AuthRepository {
    suspend fun signIn(requestUser: RequestUser): Flow<Resource<User>>
    suspend fun signUp(requestUser: RequestUser): Flow<Resource<User>>
    suspend fun signOut(): Flow<Resource<String>>
    suspend fun getCurrentUser():Flow<Resource<User?>>
}