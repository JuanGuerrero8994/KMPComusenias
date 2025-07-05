package org.example.project.domain.usecase.auth


import org.example.project.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import org.example.project.data.core.Resource
import org.example.project.domain.model.user.User

class AuthUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(action: AuthAction, data: Any? = null): Flow<Resource<Any?>> =
        when (action) {
            AuthAction.SIGN_IN -> { repository.signIn(data as User) }
            AuthAction.SIGN_UP -> { repository.signUp(data as User) }
            AuthAction.SIGN_OUT -> {repository.signOut()}
            AuthAction.GET_CURRENT_USER -> {repository.getCurrentUser()}
            AuthAction.RESET_PASSWORD -> {repository.resetPassword(data as String)}
        }
}