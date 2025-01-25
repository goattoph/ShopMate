package ipt.dam.shopmate.retrofit.service
import android.content.Context
import android.content.SharedPreferences

class UsersPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Guardar o nome do utilizador
    fun saveUserName(userName: String) {
        sharedPreferences.edit().putString("USERNAME", userName).apply()
    }

    // Ir buscar o nome do utilizador
    fun getUserName(): String? {
        return sharedPreferences.getString("USERNAME", null)
    }

    // Guardar o email do utilizador
    fun saveUserEmail(userEmail: String) {
        sharedPreferences.edit().putString("USER_EMAIL", userEmail).apply()
    }

    // Ir buscar o email do utilizador
    fun getUserEmail(): String? {
        return sharedPreferences.getString("USER_EMAIL", null)
    }

    // Guardar o estado de login
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("IS_LOGGED_IN", isLoggedIn).apply()
    }

    // Verificar o estado de login
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    // Limpar as informações guardadas
    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}