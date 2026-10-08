package simple.project.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun login() {
        val currentState = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        
        // Dummy authentication logic
        if (currentState.username == "admin" && currentState.password == "123") {
            _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
        } else {
            _uiState.update { it.copy(isLoading = false, error = "Invalid username or password") }
        }
    }
    
    fun resetLoginStatus() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}
