package ipt.dam.shopmate.retrofit.service
import ipt.dam.shopmate.models.UsersList
import ipt.dam.shopmate.models.Item
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

//import ipt.dam.api.model.APIResult

//interface UsersService {
//    @GET("https://my-favorite-things.azurewebsites.net/api/Reviews/reviews-paginated?pageNumber=1&pageSize=9&byUser=false")
//    fun list(): Call<List<Note>>

//    @GET("API/reset.php")
//    fun reset(): Call<List<Note>>

//    @FormUrlEncoded
//    @POST("API/addNote.php")
//    fun addNote(@Field("title") title: String?, @Field("description") description: String?): Call<APIResult>

//}

// Data class para representar o corpo da requisição de login
data class LoginRequest(val email: String, val password: String)

// Data class para representar a resposta do login
data class LoginResponse(
    val message: String,
    val userName: String,
    val email: String
)

// Data class para o registo
data class RegisterRequest(val userName:String, val email: String, val password: String)

// Data class para criar uma lista
data class CreateListRequest(val name: String)

// Data class para criar um item
data class CreateItemRequest(val itemName: String, val itemImage: String, val amount: Int, val isChecked: Boolean)

interface UsersService {

    // Endpoint para login
    @POST("api/Utilizadores/sign-in-user")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Endpoint para registo
    @POST("api/Utilizadores/create-user")
    fun register(@Body request: RegisterRequest): Call<Void>

    // Endpoint para logout
    @POST("api/Utilizadores/log-out-user")
    fun logOut(): Call<Void>

    // Endpoint para apagar conta
    @DELETE("api/Utilizadores/delete-user/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>

    // Endpoint para ir buscar as listas do utilizador autenticado
    @GET("api/Lists/get-lists")
    fun getLists(): Call<List<UsersList>>

    // Endpoint para criar uma lista
    @POST("api/Lists/create-list")
    fun createList(@Body request: CreateListRequest): Call<Void>

    // Endpoint para eliminar uma lista
    @DELETE("api/Lists/delete-list/{listId}")
    fun deleteList(@Path("listId") listId: Int): Call<Void>

    // Endpoint para is buscar os itens referentes a uma lista
    @GET("api/items/get-list-items/{listId}")
    fun getItems(@Path("listId") listId: Int): Call<List<Item>>

    // Endpoint para criar um item dentro de uma lista
    @Multipart
    @POST("api/items/create-item/{listId}")
    fun createItem(
        @Path("listId") listId: Int,
        @Part("itemName") itemName: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("isChecked") isChecked: RequestBody
    ): Call<Void>

    // Endpoint para eliminar um item de uma lista
    @DELETE("api/items/delete-item/{itemId}")
    fun deleteItem(@Path("itemId") itemId: Int): Call<Void>
}