package ipt.dam.shopmate.models

import com.google.gson.annotations.SerializedName

data class UsersList (
    @SerializedName("listId") val listId: Int,
    @SerializedName("listName") val listName: String?
)
