package org.example.project.ui.screen.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.project.data.core.Resource
import org.example.project.domain.model.user.User
import org.example.project.domain.usecase.AuthUseCase
import org.example.project.ui.base.BaseViewModel
import org.example.project.domain.usecase.AuthAction.*

class AuthViewModel(private val useCase: AuthUseCase) : BaseViewModel() {

    private val _signInResult = MutableStateFlow<Resource<User>>(Resource.Loading)
    val signInResult: StateFlow<Resource<User>> get() = _signInResult

    private val _signUpResult = MutableStateFlow<Resource<User>>(Resource.Loading)
    val signUpResult: StateFlow<Resource<User>> get() = _signUpResult

    private val _signOutResult = MutableStateFlow<Resource<String>>(Resource.Loading)
    val signOutResult: StateFlow<Resource<String>> get() = _signOutResult

    private val _currentUserState = MutableStateFlow<Resource<User?>>(Resource.Loading)
    val currentUserState: StateFlow<Resource<User?>> get() = _currentUserState

    init { checkCurrentUser() }

    // Funcion para iniciar sesión
    fun signIn(user: User) { fetchData(_signInResult){ (useCase.invoke(action = SIGN_IN,user) as? Flow<Resource<User>>)!! } }

    // Funcion para registrarse
    fun signUp(user: User) { fetchData(_signUpResult){ (useCase.invoke(action = SIGN_UP,user) as? Flow<Resource<User>>)!! } }

    // Funcion para cerrar sesión
    fun signOut() { fetchData(_signOutResult){ useCase.invoke(SIGN_OUT) as Flow<Resource<String>> } }

    /* Funcion para saber si un usuario fue autenticado o no , en el caso que este autenticado
      no es necesario volver a iniciar sesion*/
    private fun checkCurrentUser() { fetchData(_currentUserState){ useCase.invoke(GET_CURRENT_USER) as Flow<Resource<User?>> } }
}