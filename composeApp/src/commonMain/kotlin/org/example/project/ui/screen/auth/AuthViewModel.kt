package org.example.project.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.project.data.core.Resource
import org.example.project.data.model.auth.RequestUser
import org.example.project.domain.model.auth.User
import org.example.project.domain.usecase.AuthUseCase

class AuthViewModel(private val useCase: AuthUseCase) : ViewModel() {
    private val _signInResult = MutableStateFlow<Resource<User>>(Resource.Loading)
    val signInResult: StateFlow<Resource<User>> get() = _signInResult

    private val _signUpResult = MutableStateFlow<Resource<User>>(Resource.Loading)
    val signUpResult: StateFlow<Resource<User>> get() = _signUpResult

    private val _signOutResult = MutableStateFlow<Resource<String>>(Resource.Loading)
    val signOutResult: StateFlow<Resource<String>> get() = _signOutResult

    private val _currentUserState = MutableStateFlow<Resource<User?>>(Resource.Loading)
    val currentUserState: StateFlow<Resource<User?>> get() = _currentUserState


    init {
        checkCurrentUser()
    }


    // Funcion para iniciar sesión
     fun signIn(requestUser: RequestUser) {
        viewModelScope.launch {
            useCase.signIn(requestUser).collect { resource ->
                _signInResult.value = resource
            }
        }
    }

    // Funcion para registrarse
     fun signUp(requestUser: RequestUser) {
        viewModelScope.launch {
            useCase.signUp(requestUser).collect { resource ->
                _signUpResult.value = resource
            }
        }
    }

    // Funcion para cerrar sesión
     fun signOut() {
        viewModelScope.launch {
            useCase.signOut().collect { resource ->
                _signOutResult.value = resource
            }
        }
    }
    /* Funcion para saber si un usuario fue autenticado o no , en el caso que este autenticado
      no es necesario volver a iniciar sesion*/

    private fun checkCurrentUser() {
        viewModelScope.launch {
            useCase.getCurrentUser().collect {
                _currentUserState.value = it
            }
        }
    }
}