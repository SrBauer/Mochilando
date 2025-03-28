package com.example.mochilando.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mochilando.Components.ErrorDialog
import com.example.mochilando.Components.MyPasswordField
import com.example.mochilando.Components.MyTextField
import com.example.mochilando.R
import com.example.registeruser.ui.theme.RegisterUserTheme

@Composable
fun RegisterUserMainScreen(navController: NavController) {
    val registerUserViewModel: RegisterUserViewModel = viewModel()

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logomochilando), // Substitua pelo nome do seu arquivo
                    contentDescription = "Logo Mochilando",
                    modifier = Modifier
                        .size(200.dp) // Ajuste o tamanho conforme necessário
                        .padding(bottom = 24.dp)
                )

                RegisterUserFields(registerUserViewModel) {
                    navController.navigate("menu")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserFields(
    registerUserViewModel: RegisterUserViewModel,
    onNavigateToMenu: () -> Unit
) {
    val registerUser = registerUserViewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MyTextField(
            label = "Nome de usuário",
            value = registerUser.value.username,
            onValueChange = { registerUserViewModel.onUsernameChange(it) },
        )
        MyTextField(
            label = "User",
            value = registerUser.value.user,
            onValueChange = { registerUserViewModel.onUserChange(it) },
        )
        MyTextField(
            label = "E-mail",
            value = registerUser.value.email,
            onValueChange = { registerUserViewModel.onEmailChange(it) }
        )
        MyPasswordField(
            label = "Password",
            value = registerUser.value.password,
            errorMessage = registerUser.value.validatePassord(),
            onValueChange = { registerUserViewModel.onPasswordChange(it) }
        )
        MyPasswordField(
            label = "Confirm password",
            value = registerUser.value.confirmPassword,
            errorMessage = registerUser.value.validateConfirmPassword(),
            onValueChange = { registerUserViewModel.onConfirmPassword(it) }
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                if (registerUserViewModel.register()) {
                    Toast.makeText(ctx, "User registered", Toast.LENGTH_SHORT).show()
                    onNavigateToMenu()
                }
            }
        ) {
            Text(text = "Register user")
        }

        if (registerUser.value.errorMessage.isNotBlank()) {
            ErrorDialog(
                error = registerUser.value.errorMessage,
                onDismissRequest = { registerUserViewModel.cleanErrorMessage() }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true, device = "id:Galaxy Nexus")
fun RegisterUserPreview() {
    RegisterUserTheme {
        // Preview sem navegação para evitar erros
        RegisterUserMainScreen(navController = NavController(LocalContext.current))
    }
}
