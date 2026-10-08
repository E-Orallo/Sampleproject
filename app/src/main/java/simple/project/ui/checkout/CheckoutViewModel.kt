package simple.project.ui.checkout

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import simple.project.data.cart.CartManager

enum class CheckoutStep {
    Shipping,
    Summary,
    Confirmation
}

data class ShippingInfo(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val isDefault: Boolean = false,
)

data class CheckoutUiState(
    val currentStep: CheckoutStep = CheckoutStep.Shipping,
    val shippingInfo: ShippingInfo = ShippingInfo(),
)

class CheckoutViewModel(val cartManager: CartManager) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun updateShippingInfo(
        name: String? = null,
        phone: String? = null,
        address: String? = null,
        city: String? = null,
        state: String? = null,
        zipCode: String? = null,
        isDefault: Boolean? = null,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                shippingInfo = currentState.shippingInfo.copy(
                    name = name ?: currentState.shippingInfo.name,
                    phone = phone ?: currentState.shippingInfo.phone,
                    address = address ?: currentState.shippingInfo.address,
                    city = city ?: currentState.shippingInfo.city,
                    state = state ?: currentState.shippingInfo.state,
                    zipCode = zipCode ?: currentState.shippingInfo.zipCode,
                    isDefault = isDefault ?: currentState.shippingInfo.isDefault,
                )
            )
        }
    }

    fun nextStep() {
        _uiState.update { currentState ->
            when (currentState.currentStep) {
                CheckoutStep.Shipping -> currentState.copy(currentStep = CheckoutStep.Summary)
                CheckoutStep.Summary -> currentState.copy(currentStep = CheckoutStep.Confirmation)
                CheckoutStep.Confirmation -> currentState
            }
        }
    }

    fun previousStep() {
        _uiState.update { currentState ->
            when (currentState.currentStep) {
                CheckoutStep.Shipping -> currentState
                CheckoutStep.Summary -> currentState.copy(currentStep = CheckoutStep.Shipping)
                CheckoutStep.Confirmation -> currentState.copy(currentStep = CheckoutStep.Summary)
            }
        }
    }
}
