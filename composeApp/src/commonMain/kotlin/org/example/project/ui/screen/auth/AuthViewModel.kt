    package org.example.project.ui.screen.auth

    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import org.example.project.data.core.Resource
    import org.example.project.domain.model.user.User
    import org.example.project.domain.usecase.AuthUseCase
    import org.example.project.ui.base.BaseViewModel
    import org.example.project.domain.usecase.AuthAction.*
    import org.example.project.domain.validation.BirthDateValidator
    import org.example.project.domain.validation.CompositeValidator
    import org.example.project.domain.validation.ConfirmPasswordValidator
    import org.example.project.domain.validation.EmailValidator
    import org.example.project.domain.validation.FullNameValidator
    import org.example.project.domain.validation.NotEmptyValidator
    import org.example.project.domain.validation.PasswordValidator
    import org.example.project.domain.validation.PhoneValidator
    import org.example.project.domain.validation.SpecialtyValidator

    class AuthViewModel(private val useCase: AuthUseCase) : BaseViewModel() {

        private val _loading = MutableStateFlow(false)
        val loading: StateFlow<Boolean> get() = _loading

        //-----------------------ERROR----------------------------------//
        private val _emailError = MutableStateFlow<String?>(null)
        val emailError: StateFlow<String?> get() = _emailError

        private val _passwordError = MutableStateFlow<String?>(null)
        val passwordError: StateFlow<String?> get() = _passwordError

        private val _fullNameError = MutableStateFlow<String?>(null)
        val fullNameError: StateFlow<String?> get() = _fullNameError

        private val _confirmPasswordError = MutableStateFlow<String?>(null)
        val confirmPasswordError: StateFlow<String?> get() = _confirmPasswordError

        private val _phoneError = MutableStateFlow<String?>(null)
        val phoneError: StateFlow<String?> get() = _phoneError

        private val _birthDateError = MutableStateFlow<String?>(null)
        val birthDateError: StateFlow<String?> get() = _birthDateError

        private val _specialtyError = MutableStateFlow<String?>(null)
        val specialtyError: StateFlow<String?> get() = _specialtyError
        // -----------------------------------------------------------//

        //--------------------VALIDATOR -------------------------------//
        private val emailValidator = CompositeValidator(listOf(NotEmptyValidator(), EmailValidator()))
        private val passwordValidator = CompositeValidator(listOf(NotEmptyValidator(), PasswordValidator()))
        private val fullNameValidator = CompositeValidator(listOf(NotEmptyValidator(), FullNameValidator()))
        private val birthDateValidator = CompositeValidator(listOf(NotEmptyValidator(), BirthDateValidator()))
        private val phoneValidator = CompositeValidator(listOf(NotEmptyValidator(), PhoneValidator()))
        private val specialtyValidator = CompositeValidator(listOf(NotEmptyValidator(), SpecialtyValidator()))


        //-----------------------------------------------------------//

        private val _signInResult = MutableStateFlow<Resource<User>>(Resource.Loading)
        val signInResult: StateFlow<Resource<User>> get() = _signInResult

        private val _signUpResult = MutableStateFlow<Resource<User>>(Resource.Loading)
        val signUpResult: StateFlow<Resource<User>> get() = _signUpResult

        private val _signOutResult = MutableStateFlow<Resource<String>>(Resource.Loading)
        val signOutResult: StateFlow<Resource<String>> get() = _signOutResult

        private val _currentUserState = MutableStateFlow<Resource<User?>>(Resource.Loading)
        val currentUserState: StateFlow<Resource<User?>> get() = _currentUserState

        private val _resetPasswordState = MutableStateFlow<Resource<String>>(Resource.Loading)
        val resetPasswordState: StateFlow<Resource<String>> get() = _resetPasswordState


        fun signIn(user: User) {
            val emailResult = emailValidator.validate(user.email.orEmpty())
            val passwordResult = passwordValidator.validate(user.password.orEmpty())

            _emailError.value = emailResult.errorMessage
            _passwordError.value = passwordResult.errorMessage

            if (emailResult.isValid && passwordResult.isValid) {
                _loading.value = true
                fetchData(_signInResult) {
                    useCase.invoke(SIGN_IN, user) as Flow<Resource<User>>
                }
            } else {
                _loading.value = false
            }
        }

        // Funcion para registrarse
        fun signUp(user: User) {
            _loading.value = true
            fetchData(_signUpResult) { useCase.invoke(SIGN_UP, user) as Flow<Resource<User>> }
        }

        // Funcion para cerrar sesión
        fun signOut() {
            fetchData(_signOutResult) { useCase.invoke(SIGN_OUT) as Flow<Resource<String>> }
        }


        /* Funcion para saber si un usuario fue autenticado o no , en el caso que este autenticado
          no es necesario volver a iniciar sesion*/
        fun checkCurrentUser() {
            fetchData(_currentUserState) { useCase.invoke(GET_CURRENT_USER) as Flow<Resource<User?>> }
        }

        fun resetPassword(email: String) {
            val emailResult = emailValidator.validate(email)
            _emailError.value = emailResult.errorMessage

            if (emailResult.isValid) {
                _loading.value = true
                fetchData(_resetPasswordState) {
                    useCase.invoke(RESET_PASSWORD, email) as Flow<Resource<String>>
                }
            } else {
                _loading.value = false
            }
        }

        fun validateSignUp(
            fullName: String,
            email: String,
            password: String,
            passwordConfirm: String,
            isSpecialist: Boolean,
            phone: String,
            birthDate: String,
            specialty: String
        ): Boolean {
            var isValid = true

            val fullNameResult = fullNameValidator.validate(fullName)
            val emailResult = emailValidator.validate(email)
            val passwordResult = passwordValidator.validate(password)
            val confirmPasswordValidator =
                CompositeValidator(listOf(NotEmptyValidator(), ConfirmPasswordValidator(password)))
            val confirmPasswordResult = confirmPasswordValidator.validate(passwordConfirm)
            val phoneResult = phoneValidator.validate(phone)
            val birthDateResult = birthDateValidator.validate(birthDate)
            val specialtyResult = specialtyValidator.validate(specialty)

            _emailError.value = emailResult.errorMessage
            _passwordError.value = passwordResult.errorMessage
            _confirmPasswordError.value = confirmPasswordResult.errorMessage
            _fullNameError.value = fullNameResult.errorMessage
            _phoneError.value = phoneResult.errorMessage
            _birthDateError.value = birthDateResult.errorMessage
            _specialtyError.value = specialtyResult.errorMessage


            if (!fullNameResult.isValid) isValid = false
            if (!emailResult.isValid) isValid = false
            if (!passwordResult.isValid) isValid = false
            if (!confirmPasswordResult.isValid) isValid = false

            // Especialista: validar más campos
            if (isSpecialist) {
                if (phone.isBlank()) isValid = false
                if (birthDate.isBlank()) isValid = false
                if (specialty.isBlank()) isValid = false
            }

            return isValid
        }


    }
