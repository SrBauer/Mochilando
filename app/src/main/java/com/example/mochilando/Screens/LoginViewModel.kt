package com.example.mochilando.Screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mochilando.Dao.UserDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUser (
    val user: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isValid: Boolean = false
) {
    fun validatePassord(): String {
        if (password.isBlank()) {
            return "Password is required"
        }
        return ""
    }


    fun validateAllField() {
        if (user.isBlank()) {
            throw Exception("User is required")
        }
        if (validatePassord().isNotBlank()) {
            throw Exception(validatePassord())
        }
    }
}

class LoginViewModel(
    private val _userDAO: UserDAO
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUser())
    val uiState : StateFlow<LoginUser> = _uiState.asStateFlow()


    fun onUserChange(user : String) {
        _uiState.value = _uiState.value.copy(user = user)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login()  {
        try {
            _uiState.value.validateAllField()

            viewModelScope.launch {
                val valid = _userDAO.login(
                    _uiState.value.user,
                    _uiState.value.password
                )
                if (valid != null)
                    _uiState.value = _uiState.value.copy(isValid = true)
                else
                    _uiState.value = _uiState.value.copy(errorMessage = "Invalid Login")
            }
        }
        catch (e: Exception) {
            _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Unknow error")
        }
    }

    fun cleanErrorMessage() {
        _uiState.value = _uiState.value.copy(
            errorMessage = "",
            isValid = false
        )
    }
}