package org.example.project.ui.screen.auth

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kmpcomusenias.composeapp.generated.resources.Res
import kmpcomusenias.composeapp.generated.resources.mail_icon
import org.example.project.ui.components.customViews.LoadingDialog
import org.example.project.ui.components.scaffold.ScaffoldComponent
import org.example.project.data.core.Resource
import org.example.project.domain.model.user.Children
import org.example.project.domain.model.user.Specialist
import org.example.project.domain.model.user.User
import org.example.project.ui.components.auth.AuthButton
import org.example.project.ui.components.auth.AuthenticationHeaderContent
import org.example.project.ui.components.auth.CheckBoxComponent
import org.example.project.ui.components.auth.OutlinedTextFieldComponent
import org.example.project.ui.theme.COMPLETE_NAME
import org.example.project.ui.theme.CONFIRM_PASS
import org.example.project.ui.theme.ESPECIALIST
import org.example.project.ui.theme.PASSWORD
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthViewModel = koinViewModel()) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var isLoading by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var isSpecialist by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf("") }
    val specialties = listOf("Especialista 1", "Especialista 2", "Especialista 3")

    val signUpResult by viewModel.signUpResult.collectAsState()

    ScaffoldComponent(navController = navController, showTopBar = false, showBottomBar = false) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).pointerInput(Unit) { detectTapGestures {keyboardController?.hide() }} , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            AuthenticationHeaderContent()
            OutlinedTextFieldComponent(label = COMPLETE_NAME, value = fullName, onValueChange = { fullName = it }, keyboardType = KeyboardType.Text)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextFieldComponent(value = email, onValueChange = { email = it }, label = "Correo Electrónico", keyboardType = KeyboardType.Email, trailingIcon = { Icon(painter = painterResource(resource = Res.drawable.mail_icon), contentDescription = "Email icon") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextFieldComponent(label = PASSWORD, value = password, onValueChange = { password = it }, keyboardType = KeyboardType.Password, isPassword = true)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextFieldComponent(label = CONFIRM_PASS, value = passwordConfirm, onValueChange = { passwordConfirm = it }, keyboardType = KeyboardType.Password, isPassword = true)

            if (isSpecialist) {
                OutlinedTextFieldComponent(value = phone, onValueChange = { phone = it }, keyboardType = KeyboardType.Phone, label = "Nro Telefono", isPhone = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextFieldComponent(value = birthDate, onValueChange = { birthDate = it }, label = "Fecha de nacimiento", isBirth = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextFieldComponent(value = selectedSpecialty, onValueChange = { selectedSpecialty = it }, label = "Selecciona una especialidad", isDropdown = true, dropdownOptions = specialties, onDropdownItemSelected = { selectedSpecialty = it })
            }

            CheckBoxComponent (label = ESPECIALIST, isChecked = isSpecialist, onCheckChange = { isSpecialist = it })

            Spacer(modifier = Modifier.height(16.dp))

            AuthButton(text = "Registrar", isEnabled = !isLoading, onClick = {
                isLoading = true
                val user: User = if (isSpecialist) {
                    Specialist(
                        idSpecialist = "",
                        speciality = selectedSpecialty,
                        childrenInCharge = emptyList(),
                        email = email,
                        password = password,
                        displayName = fullName
                    )
                } else {
                    Children(
                        idChildren = "",
                        isPremium = true,
                        email = email,
                        password = password,
                        displayName = fullName
                    )
                }
                viewModel.signUp(user)
            })

            ShowResults(signUpResult, isLoading, navController)
        }
    }
}


@Composable
private fun ShowResults(signInResult: Resource<User>, isLoading: Boolean, navController: NavController) {
    when (signInResult) {
        is Resource.Loading -> LoadingDialog(isLoading = isLoading)
        is Resource.Success -> { navController.navigateUp() }
        is Resource.Error -> {Text(text = "${signInResult.exception.message}") }
    }
}

