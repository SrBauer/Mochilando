package com.example.mochilando.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mochilando.Components.ErrorDialog
import com.example.mochilando.Components.MyPasswordField
import com.example.mochilando.Components.MyTextField
import com.example.mochilando.R
import com.example.registeruser.ui.theme.RegisterUserTheme

@Composable
fun RegisterUserMainScreen(navController: NavController) {
    val registerUserViewModel: RegisterUserViewModel = viewModel()

    Scaffold(
        containerColor = Color(0xFFF5F5F5) // Cor de fundo suave
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 62.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logomochilando),
                    contentDescription = "Logo Mochilando",
                    modifier = Modifier
                        .size(240.dp)
                        .padding(bottom = 12.dp)
                )

                RegisterUserFields(registerUserViewModel) {
                    navController.navigate("menu")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Já tem uma conta? Faça login",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
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
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyTextField(
            label = "Nome de usuário",
            value = registerUser.value.username,
            onValueChange = { registerUserViewModel.onUsernameChange(it) },
        )

        Spacer(modifier = Modifier.height(4.dp))

        MyTextField(
            label = "User",
            value = registerUser.value.user,
            onValueChange = { registerUserViewModel.onUserChange(it) },
        )

        Spacer(modifier = Modifier.height(4.dp))

        MyTextField(
            label = "E-mail",
            value = registerUser.value.email,
            onValueChange = { registerUserViewModel.onEmailChange(it) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        MyPasswordField(
            label = "Senha",
            value = registerUser.value.password,
            errorMessage = registerUser.value.validatePassord(),
            onValueChange = { registerUserViewModel.onPasswordChange(it) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        MyPasswordField(
            label = "Confirme sua senha",
            value = registerUser.value.confirmPassword,
            errorMessage = registerUser.value.validateConfirmPassword(),
            onValueChange = { registerUserViewModel.onConfirmPassword(it) }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF135937)), // Cor do botão
            onClick = {
                if (registerUserViewModel.register()) {
                    Toast.makeText(ctx, "Usuário cadastrado!", Toast.LENGTH_SHORT).show()
                    onNavigateToMenu()
                }
            }
        ) {
            Text(text = "Registrar", fontSize = 18.sp, color = Color.White)
        }

        if (registerUser.value.errorMessage.isNotBlank()) {
            ErrorDialog(
                error = registerUser.value.errorMessage,
                onDismissRequest = { registerUserViewModel.cleanErrorMessage() }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterUserPreview() {
    RegisterUserTheme {
        RegisterUserMainScreen(navController = rememberNavController())
    }
}
