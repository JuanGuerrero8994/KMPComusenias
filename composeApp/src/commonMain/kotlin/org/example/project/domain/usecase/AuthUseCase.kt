package org.example.project.domain.usecase


import org.example.project.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import org.example.project.data.core.Resource
import org.example.project.data.model.auth.RequestUser
import org.example.project.domain.model.auth.User

class AuthUseCase(private val repository: AuthRepository) {

    //signIn
    suspend  fun signIn(requestUser: RequestUser): Flow<Resource<User>> = repository.signIn(requestUser)

    // Método para signUp
    suspend fun signUp(requestUser: RequestUser): Flow<Resource<User>> = repository.signUp(requestUser)

    // Método para signOut
    suspend fun signOut(): Flow<Resource<String>> = repository.signOut()

    //Metodo para saber si el usuario fue autenticado o no
    suspend fun getCurrentUser(): Flow<Resource<User?>> = repository.getCurrentUser()

}