package ipt.dam.shopmate.models

import com.google.gson.annotations.SerializedName

// Classe que representa um utilizador
data class Users (
    @SerializedName("id") val userId: Int?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)
