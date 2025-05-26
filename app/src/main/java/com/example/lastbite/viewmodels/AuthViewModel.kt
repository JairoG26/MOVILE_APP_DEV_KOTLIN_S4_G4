package com.example.lastbite.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.SignUpData
import com.example.lastbite.models.User
import com.example.lastbite.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authStateRegister = MutableLiveData<Boolean>()
    val authStateRegister: LiveData<Boolean> get() = _authStateRegister
    private val _authStateLogIn = MutableLiveData<Boolean>()
    val authStateLogIn: LiveData<Boolean> get() = _authStateLogIn
    private val _authStateBack = MutableLiveData<Boolean>()
    val authStateBack: LiveData<Boolean> get() = _authStateBack
    val _userType = MutableLiveData<String>()
    val userType: LiveData<String> = _userType
    private val _stateUserSignedIn = MutableLiveData<Boolean>()
    val stateUserSignedIn: LiveData<Boolean> get() = _stateUserSignedIn
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val userRepository = UserRepository()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun registerUser(signUpData: SignUpData) {

        auth.createUserWithEmailAndPassword(signUpData.user_email, signUpData.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStateRegister.value = true
                    saveUserToBackend(signUpData)
                } else {
                    _errorMessage.value = task.exception?.localizedMessage
                }
            }
    }

    fun signInUser(email: String, password: String, appContext: Context) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStateLogIn.value = true
                    userRepository.signInUser(email) { response, user ->
                        _stateUserSignedIn.value = response
                        _user.value = user
                        Log.d("AuthVM.signInUser", "User retrieved: ${_user.value}")
                        _userType.value = user.user_type // "STORE" or "CUSTOMER"
                        userRepository.storeUserID(user.user_id, appContext)
                        Log.d("AuthVM.signInUser", "The User ID stored was: ${user.user_id}")
                    }
                } else {
                    _authStateLogIn.value = false
                    _userType.value = "UserType.CUSTOMER" // By default if the user was not found or the API response failed
                }
            }
    }

    private fun saveUserToBackend(signUpData: SignUpData) {

        userRepository.storeUserInBackend(signUpData) { callback ->
            _authStateBack.value = callback
        }
    }
}