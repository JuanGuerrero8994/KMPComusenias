package org.example.project.domain.usecase


import org.example.project.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import org.example.project.data.core.Resource
import org.example.project.domain.model.user.User

class AuthUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(action: AuthAction, user: User? = null): Flow<Resource<Any?>> =
        when (action) {
            AuthAction.SIGN_IN -> { repository.signIn(user!!) }
            AuthAction.SIGN_UP -> { repository.signUp(user!!) }
            AuthAction.SIGN_OUT -> {repository.signOut()}
            AuthAction.GET_CURRENT_USER -> {repository.getCurrentUser()}
        }
}