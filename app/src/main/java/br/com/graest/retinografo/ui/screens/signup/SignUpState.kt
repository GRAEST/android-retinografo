package br.com.graest.retinografo.ui.screens.signup

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val surname: String = "",
    val cpf: String = "",
    val cep: String = "",
    val crmList: List<String> = emptyList(),
    val crm: String = "",
    val imagePath: String? = null,
    val imageErrorMessage: String? = null,
    val isEditingImage: Boolean = false,
    val showDialog : Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val requestMessage: String = ""
)
