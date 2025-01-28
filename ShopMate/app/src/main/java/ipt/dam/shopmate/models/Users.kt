package ipt.dam.shopmate.models

import com.google.gson.annotations.SerializedName

data class Users (
    @SerializedName("id") val userId: Int?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)
