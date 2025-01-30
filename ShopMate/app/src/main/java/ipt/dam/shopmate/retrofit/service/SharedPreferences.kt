package ipt.dam.shopmate.retrofit.service
import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.HttpUrl

class SharedPreferences(context: Context) {

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

    // Métodos para armazenar cookies

    fun saveCookies(url: HttpUrl, cookies: List<Cookie>) {
        val cookieSet = cookies.map { it.toString() }.toSet()
        sharedPreferences.edit().putStringSet(url.host, cookieSet).apply()
    }

    fun getCookies(url: HttpUrl): List<Cookie> {
        val cookieSet = sharedPreferences.getStringSet(url.host, emptySet()) ?: emptySet()
        return cookieSet.mapNotNull { Cookie.parse(url, it) }
    }

    fun clearCookies() {
        sharedPreferences.edit().remove("COOKIES").apply()
    }
}