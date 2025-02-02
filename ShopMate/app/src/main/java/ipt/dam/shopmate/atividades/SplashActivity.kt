package ipt.dam.shopmate.atividades
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.LoginRequest
import ipt.dam.shopmate.retrofit.service.LoginResponse
import ipt.dam.shopmate.retrofit.service.SecureStorage
import ipt.dam.shopmate.retrofit.service.UsersPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar se o utilizador está logado
        val userPreferences = UsersPreferences(this@SplashActivity)
        val isLoggedIn = userPreferences.isLoggedIn()

        // Se o utilizador estiver logado, vai para a MainActivity
        if (isLoggedIn) {
            val userPreferences = UsersPreferences(this)
            val email = userPreferences.getUserEmail()
            val secureStorage = SecureStorage(this)
            val password: String? = secureStorage.password
            if (email != null && password != null) {
                login(email, password)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Caso contrário, vai para a LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Finaliza a SplashActivity para não voltar para ela
        finish()
    }
    private fun login(email: String, password: String) {
        val request = LoginRequest(email, password)

        RetrofitInitializer.usersService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (!response.isSuccessful) {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@SplashActivity, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
        })
    }
}
