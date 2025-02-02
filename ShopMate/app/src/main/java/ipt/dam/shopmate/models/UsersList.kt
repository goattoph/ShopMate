package ipt.dam.shopmate.models

import com.google.gson.annotations.SerializedName

// Classe que representa uma lista de compras
data class UsersList (
    @SerializedName("listId") val listId: Int,
    @SerializedName("listName") val listName: String
)
