import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mochilando.Components.ErrorDialog
import com.example.mochilando.Components.MyTextField
import com.example.mochilando.DataBase.AppDatabase
import com.example.mochilando.R
import com.example.mochilando.Screens.LoginUser
import com.example.mochilando.Screens.LoginViewModel
import com.example.mochilando.Screens.LoginViewModelFactory
import com.example.mochilando.Screens.RegisterUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun LoginScreen(navController: NavController) {
    val ctx = LocalContext.current
    val userDao = AppDatabase.getDatabase(ctx).userDao()
    val loginViewModel : LoginViewModel = viewModel(
        factory = LoginViewModelFactory(userDao))
    val loginUser = loginViewModel.uiState.collectAsState()


    Scaffold(
        containerColor = Color(0xFFF5F5F5)
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
                        .size(260.dp)
                        .padding(bottom = 24.dp)
                )

                MyTextField(
                    label = "Nome de Usuário",
                    value = loginUser.value.user,
                    onValueChange = { loginViewModel.onUserChange(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = loginUser.value.password,
                    onValueChange = { loginViewModel.onPasswordChange(it)},
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF135937)), // Cor personalizada
                    onClick = {
                        loginViewModel.login()
                    }
                ) {
                    Text(text = "Entrar", fontSize = 18.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Registrar um novo usuário",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }

            if (loginUser.value.errorMessage.isNotBlank()) {
                ErrorDialog(
                    error = loginUser.value.errorMessage,
                    onDismissRequest =  {
                        loginViewModel.cleanErrorMessage()
                    }
                )
            }
        }

        LaunchedEffect(loginUser.value.isValid) {
            if(loginUser.value.isValid){
                Toast.makeText(ctx, "User logged",
                    Toast.LENGTH_SHORT).show()

                navController.navigate("menu")
            }
        }
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}
