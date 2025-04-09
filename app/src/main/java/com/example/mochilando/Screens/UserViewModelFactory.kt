package com.example.mochilando.Screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mochilando.Dao.UserDAO

class UserViewModelFactory(
    private val userDao: UserDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterUserViewModel(userDao) as T
    }
}